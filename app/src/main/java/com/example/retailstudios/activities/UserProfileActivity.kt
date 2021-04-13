package com.example.retailstudios.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.retailstudios.R
import com.example.retailstudios.firestore.FirestoreClass
import com.example.retailstudios.models.User
import com.example.retailstudios.utils.Constants
import com.example.retailstudios.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException


class UserProfileActivity : BaseActivity(), View.OnClickListener {

     private lateinit var muserDetails: User
     private var mSelectedImageFileUri: Uri? = null
    private var mUserProfileImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
        //get the user details from intent as a ParcelableExtra
            muserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }
        et_first_name.isEnabled = false
        et_first_name.setText( muserDetails.firstName)

        et_last_name.isEnabled = false
        et_last_name.setText(muserDetails.lastName)

        et_email.isEnabled = false
        et_email.setText(muserDetails.email)

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_submit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.iv_user_photo -> {

                    //check is the permission is already allowed or we need to request for it.
                  if (ContextCompat.checkSelfPermission(this,
                          Manifest.permission.READ_EXTERNAL_STORAGE)
                  == PackageManager.PERMISSION_GRANTED)
                  {
                      //showErrorSnackBar("You already have the storage permission.",false)
                      Constants.showImageChooser(this)
                  } else{
                      /* Requests permissions to be granted to this application.These permissions
                      must be requested in your manifest,they should not be granted to your app,
                      and they should have protection level */
                          ActivityCompat.requestPermissions(this,
                              arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                                  Constants.READ_STORAGE_PERMISSION_CODE)
                }
                }
                R.id.btn_submit -> {
                    if (validateUserProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (mSelectedImageFileUri != null)
                        FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileUri)
                        else{
                            updateUserProfileDetails()
                        }
                    }
                }
            }
        }
    }

    private fun updateUserProfileDetails(){
        //showErrorSnackBar("Your details are valid. You can update them",false)
        val userHashMap = HashMap<String,Any>()

        val mobileNumber = et_mobile_number.text.toString().trim{ it <= ' '}
        val gender = if (rb_male.isChecked){
            Constants.MALE
        }else{
            Constants.FEMALE
        }
        if (mUserProfileImageURL.isNotEmpty()){
           userHashMap[Constants.IMAGE] =  mUserProfileImageURL
        }

        if (mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        //key:gender value:male
        userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.COMPLETE_PROFILE] = 1
        //showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().updateUserProfileData(this,userHashMap)
    }
    fun userProfileUpdateSuccess(){
        hideProgressDialog()

        Toast.makeText(this@UserProfileActivity,
            resources.getString(R.string.msg_profile_update_success),
            Toast.LENGTH_SHORT).show()

        startActivity(Intent(this@UserProfileActivity,MainActivity::class.java))
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //if permission is granted
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //showErrorSnackBar("The storage is granted",false)
            Constants.showImageChooser(this)
        }else{
            //Displaying another toast if permission is not granted
            Toast.makeText(this,
                resources.getString(R.string.read_storage_permission_denied),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    try {
                        //the uri of selected image from phone storage
                        mSelectedImageFileUri = data.data!!
                        //iv_user_photo.setImageURI(selectedImageFileUri)
                        GlideLoader(this).loadedUserPicture(mSelectedImageFileUri!!,iv_user_photo)

                    }catch (e:IOException){
                        e.printStackTrace()
                        Toast.makeText(this,
                            resources.getString(R.string.image_selection_failed),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }else if(resultCode == Activity.RESULT_CANCELED){
                    //a log is printed when user close or cancel the image selection
                    Log.e("Request Cancelled","Image selection cancelled")
                }
            }
        }
    }

    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_mobile_number.text.toString().trim(){ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number),true)
                false
            } else -> {
                true
            }
        }

    }

    fun imageUploadSuccess(imageURL: String) {
        //hideProgressDialog()
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}