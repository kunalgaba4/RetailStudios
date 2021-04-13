package com.example.retailstudios.ui.activities

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.retailstudios.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*

open class BaseActivity : AppCompatActivity() {

    private var doubleBackToExitPressedOnce = false
    private lateinit var mProgressDialog:Dialog

    //snackbar to show color according to success or error
    fun showErrorSnackBar(message:String,errorMessage: Boolean){
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content),message,Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    //function to show progress bar while registering user to database
    fun showProgressDialog(text:String){
        mProgressDialog = Dialog(this)

        /*Set the screen conetnt from a layout resource.
        The resource will be inflated,adding all top-level view to the screen */
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.tv_progress_text.text = text
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //start the dialog and display it on screen
        mProgressDialog.show()
    }

    //function to remove progressbar from the screen
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun doubleBactToExit(){
        if (doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce = true

        Toast.makeText(this,
            resources.getString(R.string.please_click_back_again_to_exit),
            Toast.LENGTH_SHORT).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce = false},2000)
    }
}