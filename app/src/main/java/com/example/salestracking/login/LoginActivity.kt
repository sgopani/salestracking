package com.example.salestracking.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.salestracking.MainActivity
import com.example.salestracking.R
import com.example.salestracking.register.RegisterEmployee
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var emailId: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var registerLink:TextView


    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()
    }

    private fun initialize() {
        auth = FirebaseAuth.getInstance()
        emailId = findViewById(R.id.email_add_login)
        passwordEditText = findViewById(R.id.login_password)
        loginButton = findViewById(R.id.login_button)
        registerLink=findViewById(R.id.registerLink)
        loginButton.setOnClickListener {
            //Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
            loginUser()
        }
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterEmployee::class.java)
            startActivity(intent)
            finish()
        }

    }

    public override fun onStart() {
        super.onStart()
    }

    private fun loginUser() {
        val email = emailId.text.toString()
        val password = passwordEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() || password.length < 7) {
            emailId.error = "Enter valid details"
            passwordEditText.error = "Enter valid details"
            return
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Log.d(TAG, "Logging in user.")
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                //mProgressBar!!.hide()
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        " Welcome ${auth.currentUser?.email}",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUI()
                    //}
                }
            }.addOnFailureListener(OnFailureListener {exeption->
                //Log.d("exeption", exeption.localizedMessage)
                when {
                    exeption.localizedMessage=="The password is invalid or the user does not have a password." -> {
                        Toast.makeText(this, "Enter Valid Password",Toast.LENGTH_SHORT).show()
                    }
                    exeption.localizedMessage=="There is no user record corresponding to this identifier. The user may have been deleted." -> {
                        Toast.makeText(this, "User Doesn't exist contact admin",Toast.LENGTH_SHORT).show()
                    }
                    exeption==FirebaseAuthInvalidCredentialsException(email,password) -> {
                        Toast.makeText(this, "Enter Valid Credentials",Toast.LENGTH_SHORT).show()
                    }
                    exeption==FirebaseAuthUserCollisionException (email,password) -> {
                        Toast.makeText(this, "User Already exist",Toast.LENGTH_SHORT).show()
                    }
                    exeption== FirebaseAuthInvalidUserException(email,password) -> {
                        Toast.makeText(this, "User doesn't exist contact admin ",Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(this, exeption.localizedMessage,Toast.LENGTH_SHORT).show()
                    }
                }

            })
        }
        else {
            emailId.error="Field cannot be empty"
            passwordEditText.error="Field cannot be empty"
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
