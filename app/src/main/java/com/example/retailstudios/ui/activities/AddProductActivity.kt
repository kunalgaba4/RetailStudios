package com.example.retailstudios.ui.activities

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
import com.example.retailstudios.R
import com.example.retailstudios.firestore.FirestoreClass
import com.example.retailstudios.utils.Constants
import com.example.retailstudios.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.io.IOException

class AddProductActivity : BaseActivity(), View.OnClickListener {

    private var mSelectedImageFileURI: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        setupActionBar()
        iv_add_update_product.setOnClickListener(this)
        btn_submit_add_product.setOnClickListener(this)
    }
    //setup action bar to display arrow back and going to login page
    private fun setupActionBar(){
        setSupportActionBar(toolbar_add_product_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar_add_product_activity.setNavigationOnClickListener{onBackPressed()}
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.iv_add_update_product -> {
                //check is the permission is already allowed or we need to request for it.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
                {
                    //showErrorSnackBar("You already have the storage permission.",false)
                    Constants.showImageChooser(this@AddProductActivity)
                } else{
                    /* Requests permissions to be granted to this application.These permissions
                    must be requested in your manifest,they should not be granted to your app,
                    and they should have protection level */
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                        Constants.READ_STORAGE_PERMISSION_CODE)
                }
            }
                R.id.btn_submit_add_product -> {
                    if (validateProductDetails()){
                       uploadProductImage()
                    }
                }
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this,mSelectedImageFileURI,Constants.PRODUCT_IMAGE)
    }

    fun imageUploadSuccess(imageURL: String) {
        hideProgressDialog()
        showErrorSnackBar("Product image is uploaded successfully. Image URL: $imageURL",false)
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
                    iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(this,R.drawable.ic_edit))
                        mSelectedImageFileURI = data.data!!
                    try {
                        GlideLoader(this).loadedUserPicture(mSelectedImageFileURI!!,iv_product_image)
                    }catch (e:IOException){
                        e.printStackTrace()
                    }
                }else if(resultCode == Activity.RESULT_CANCELED){
                    //a log is printed when user close or cancel the image selection
                    Log.e("Request Cancelled","Image selection cancelled")
                }
            }
        }
    }

    private fun validateProductDetails():Boolean
    {
       return when {
           mSelectedImageFileURI == null -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_select_product_image), true)
               false
           }
           TextUtils.isEmpty(et_product_title.text.toString().trim() { it <= ' ' }) -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_title), true)
               false
           }
           TextUtils.isEmpty(et_product_price.text.toString().trim() { it <= ' ' }) -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_price), true)
               false
           }
           TextUtils.isEmpty(et_product_description.text.toString().trim() { it <= ' ' }) -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_description), true)
               false
           }
           TextUtils.isEmpty(et_product_quantity.text.toString().trim() { it <= ' ' }) -> {
               showErrorSnackBar(resources.getString(R.string.err_msg_enter_product_quantity), true)
               false
           }
           else -> {
               true
           }
         }
       }
    }