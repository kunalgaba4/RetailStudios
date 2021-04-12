package com.example.retailstudios.firestore

import android.app.Activity
import android.util.Log
import com.example.retailstudios.activities.LoginActivity
import com.example.retailstudios.activities.RegisterActivity
import com.example.retailstudios.models.User
import com.example.retailstudios.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity,userInfo: User){
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
}