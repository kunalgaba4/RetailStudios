package com.example.retailstudios.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.example.retailstudios.ui.activities.LoginActivity
import com.example.retailstudios.ui.activities.RegisterActivity
import com.example.retailstudios.ui.activities.UserProfileActivity
import com.example.retailstudios.models.User
import com.example.retailstudios.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){
        //The "users" is collection name, if the collection is already created then it will not create the same one again.
        mFirestore.collection(Constants.USERS)
            //document ID for users fields.Here the document it is the User ID
            .document(userInfo.id)
            //here the userinfo are field and the SetOption is set to merge. It is for if we want to merge later or instead of replacing the fields.
            .set(userInfo, SetOptions.merge()).addOnSuccessListener {
                //call a function of base activity for transferring the result to it
                activity.userRegisterationSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {
        //an instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser
        //a variable to assign the currentUserId if it is not null or else it will be blank
        var currentUserID = ""
        if (currentUserID != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity : Activity){

        //we pass the collection name from which we wants the data.
        mFirestore.collection(Constants.USERS)
        //the document id to get the fields of user
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())

                //we have received the document snapshot which converted into the User Data model object
                val user = document.toObject(User::class.java)!!


                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.RETAILSTUDIOS_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                //data storeage in device
                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //key:value  logged_in_username :firstname,lastname
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                //TODO Step 6: Pass the result to the Login Activity.
                //START
                when(activity){
                    is LoginActivity -> {
                        //call a function of base activity for transferring the result to it
                        activity.userLoggedInSuccess(user)
                    }
                }
                //END
            }.addOnFailureListener{ e ->
                //hide the progress dialog if there is any error and print the error in log
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity,userHashMap: HashMap<String,Any>){
        mFirestore.collection(Constants.USERS).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener{ e ->
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating the user details.",
                    e)

            }
    }

    fun uploadImageToCloudStorage(activity: Activity,imageFileURI: Uri?){
        val sRef:StorageReference = FirebaseStorage.getInstance().reference.child(
                Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() + "."
                + Constants.getFileExtension(
                    activity,imageFileURI
                )
        )
        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            //the image upload is success
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )

            //get the downloadable uri from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable image URL",uri.toString())
                    when(activity){
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }

                }
        }.addOnFailureListener{ exception ->
            //hide the progress dialog if there is any error and print the error in log
            when(activity){
                is UserProfileActivity -> {
                    activity.hideProgressDialog()
                }
            }
            Log.e(
                activity.javaClass.simpleName,
                exception.message,
                exception
            )
        }

    }


}