package com.example.salesadmin.login

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.salesadmin.R
import com.google.firebase.auth.FirebaseAuth


class ForgotPassword : AppCompatActivity(), View.OnClickListener {
    private lateinit var email:EditText
    private lateinit var sendMail:Button
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forgot_password)
        email=findViewById(R.id.email_forgot_password)
        sendMail=findViewById(R.id.send_email_button)
        progressBar=findViewById(R.id.progress_bar)
        sendMail.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id){

            R.id.send_email_button -> {

                if (email.text.isEmpty()) {
                    email.error = "Cannot be blank"
                    return
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.text.trim()).matches()) {
                    email.error = "Enter valid email"
                    return
                } else {
                    progressBar.visibility=View.VISIBLE
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Email sent Successfully", Toast.LENGTH_LONG)
                                    .show()
                                progressBar.visibility=View.GONE
                            }
                        }
                }.addOnFailureListener {exception->
                    Log.d("exception","$exception")
                    Toast.makeText(this, "There is no user corresponding to this identifier", Toast.LENGTH_LONG)
                        .show()
                    progressBar.visibility=View.GONE
                }
            }
        }
    }
}