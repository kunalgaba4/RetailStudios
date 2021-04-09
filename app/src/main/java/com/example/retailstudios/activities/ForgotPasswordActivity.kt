package com.example.retailstudios.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.retailstudios.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class ForgotPasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        setupActionBar()
    }

    //setup action bar to display arrow back and going to login page
    private fun setupActionBar() {
        setSupportActionBar(toolbar_forgot_password_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        toolbar_forgot_password_activity.setNavigationOnClickListener { onBackPressed() }

        //click event assign to submit button
        btn_submit.setOnClickListener{

            //Get the text from editText and trim the space
            val email:String = etf_email.text.toString().trim{it <= ' '}

            //if user leave email field blank then show error
            if (email.isEmpty()){
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
            }else{
                //show progressBar
                showProgressDialog(resources.getString(R.string.please_wait))

                //send reset password link to email address
                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->

                        //calling function from baseactivity to disappear a progressbar
                        hideProgressDialog()


                        if (task.isSuccessful){
                            //if the reset password link is successfully sent then show the toast message and finish the forgot password activity
                            Toast.makeText(this@ForgotPasswordActivity,
                                resources.getString(R.string.email_sent_success),
                                Toast.LENGTH_LONG)
                                .show()
                            //go back to the login screen
                            finish()
                        } else{
                            //if the reset password link is not successful sent then show error message
                            showErrorSnackBar(task.exception!!.message.toString(),true)
                        }
                    }
            }
        }
    }



}