package com.example.retailstudios.firestore

import android.util.Log
import com.example.retailstudios.activities.RegisterActivity
import com.example.retailstudios.models.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity,userInfo: User){
        //The "users" is collection name, if the collection is already created then it will not create the same one again.
        mFirestore.collection("users")
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
    
}