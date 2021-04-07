package com.example.retailstudios.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import com.example.retailstudios.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

        //function calling
        setupActionBar()

        //go to login page
        tv_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(intent)
        }

        //validate details on button click and register user to database
        btn_register.setOnClickListener{
           registerUser()
        }
    }

    //setup action bar to display arrow back and going to login page
    private fun setupActionBar(){
        setSupportActionBar(tv_toolbar_register_activity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        tv_toolbar_register_activity.setNavigationOnClickListener{onBackPressed()}
    }

    //function to validate the entries of user
    private fun validateRegisterDetails(): Boolean{
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString().trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch),true)
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition),true)
                false
            }
            else ->
            {
               //showErrorSnackBar(resources.getString(R.string.registering_successful),false)
                true
            }
        }
    }

    //registering the user using firebase
    private fun registerUser(){

        //check with validate function if the entries are valid or not
        if (validateRegisterDetails()){

            //calling function from baseactivity to show progressbar
            showProgressDialog(resources.getString(R.string.please_wait))

            val email:String = et_email.text.toString().trim{it <= ' '}
            val password:String = et_password.text.toString().trim{it <= ' '}

            //create an instance and create a register a user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    OnCompleteListener <AuthResult>{ task ->

                        //calling function from baseactivity to disappear a progressbar
                        hideProgressDialog()

                        //if the registration is successfully done
                        if (task.isSuccessful){
                            //firebase registered user
                            val firebaseUser:FirebaseUser = task.result!!.user!!

                            showErrorSnackBar("You are registered successfully. Your user id is ${firebaseUser.uid}",false)

                            //go back to login activity
                            FirebaseAuth.getInstance().signOut()
                            finish()
                        }
                        else{
                            //if the registering is not successful then show error message
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
                )
        }
    }
}