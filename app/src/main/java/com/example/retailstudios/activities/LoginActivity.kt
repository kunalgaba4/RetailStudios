package com.example.retailstudios.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.retailstudios.R
import com.example.retailstudios.firestore.FirestoreClass
import com.example.retailstudios.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //fullscreen on android
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        //click event assigned to ForgetPassword text
        tv_forget_password.setOnClickListener(this)
        //click event assigned to Login button
        btn_login.setOnClickListener(this)
        //click event assigned to Register text
        tv_register.setOnClickListener(this)

    }

    fun userLoggedInSuccess(user: User){
        //hide the progress dialog
        hideProgressDialog()

        //print the user datials in the log as of now
        Log.i("First Name: ",user.firstName)
        Log.i("Last Name: ",user.lastName)
        Log.i("Email: ",user.email)

        //redirect the user to Main Screen after Log in
        startActivity(Intent(this@LoginActivity,MainActivity::class.java))
        finish()

    }

    //In login screen the clickable components are Login Button, ForgetPassword and Register Text
    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {

                R.id.tv_forget_password -> {
                    // Launch the forgotPassword screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }
                R.id.btn_login -> {
                    logInRegisteredUser()
                }
                R.id.tv_register -> {
                    // Launch the register screen when the user clicks on the text
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(etl_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(etl_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun logInRegisteredUser(){

        if (validateLoginDetails()){

            //calling function from baseactivity to show progressbar
            showProgressDialog(resources.getString(R.string.please_wait))

            //Get the text from editText and trim the space
            val email:String = etl_email.text.toString().trim{it <= ' '}
            val password:String = etl_password.text.toString().trim{it <= ' '}

            //LogIn using firebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->

                    if (task.isSuccessful){

                        FirestoreClass().getUserDetails(this@LoginActivity)

                    } else{
                        hideProgressDialog()
                        //if the registering is not successful then show error message
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }
}