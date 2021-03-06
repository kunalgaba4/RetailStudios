package com.example.retailstudios.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    // Collection in Cloud Firestore
    const val USERS: String = "users"
    const val PRODUCTS: String = "products"

    const val RETAILSTUDIOS_PREFERENCES:String = "RetailStudioPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1

    const val MALE: String = "Male"
    const val FEMALE: String = "Female"
    const val FIRST_NAME:String = "firstName"
    const val LAST_NAME:String = "lastName"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val IMAGE:String = "image"
    const val COMPLETE_PROFILE:String = "profileCompleted"

    const val PRODUCT_IMAGE:String = "Product_Image"

    const val USER_ID: String = "user_id"

    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"

    fun showImageChooser(activity:Activity){
        //an intent for launching the image selection of phone storage.
        val galleryIntent = Intent(Intent.ACTION_PICK,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        //launches the image selection of phone storage using the constant code
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    /*
    MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa

    getSingleton(): Get the singleton instance of MimeTypeMap

    getExtensionFromMimeType: Return the registered extension for the given MIME type

    ContentResolver.getType: Return the MINE type of the given content URL*/

    fun getFileExtension(activity: Activity,uri:Uri?): String?{
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }
}