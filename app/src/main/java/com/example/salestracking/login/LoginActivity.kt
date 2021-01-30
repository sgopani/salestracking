package com.example.salestracking.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.example.salestracking.COMPANYUID
import com.example.salestracking.MainActivity
import com.example.salestracking.PrefManager
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
    private lateinit var fstore: FirebaseFirestore
    private lateinit var progressBar: ProgressBar
    private lateinit var forgotPassword: TextView
    private lateinit var companyID:EditText
    private lateinit var prefManager: PrefManager


    private val TAG = "LoginActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        initialize()
    }

    private fun initialize() {
        auth = FirebaseAuth.getInstance()
        emailId = findViewById(R.id.email_add_login)
        passwordEditText = findViewById(R.id.et_amount)
        loginButton = findViewById(R.id.login_button)
        registerLink=findViewById(R.id.registerLink)
        progressBar=findViewById(R.id.progress_bar)
        fstore=FirebaseFirestore.getInstance()
        forgotPassword=findViewById(R.id.forgot_password_tv)
        companyID=findViewById(R.id.company_id_login)
        prefManager = PrefManager(this)
        loginButton.setOnClickListener {
            //Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
            loginUser()
        }
        registerLink.setOnClickListener {
            val intent = Intent(this, RegisterEmployee::class.java)
            startActivity(intent)
            finish()
        }
        forgotPassword.setOnClickListener{
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

    }

    public override fun onStart() {
        super.onStart()
    }

    private fun loginUser() {
        val email = emailId.text.toString()
        val password = passwordEditText.text.toString()
        val id=companyID.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() || password.length < 7) {
            emailId.error = "Enter valid details"
            passwordEditText.error = "Enter valid details"
            return
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id)) {
            Log.d(TAG, "Logging in user.")
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                //mProgressBar!!.hide()
                progressBar.visibility=View.VISIBLE
                if (task.isSuccessful) {
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
                progressBar.visibility=View.GONE

            })
        }
        else {
            emailId.error="Field cannot be empty"
            passwordEditText.error="Field cannot be empty"
            companyID.error="Field cannot be empty"
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI() {
        val documentReference=fstore.collection("Sales").document(companyID.text.toString())
                .collection("employee").document(emailId.text.toString())
        documentReference.get().addOnSuccessListener {document->
            if(document.exists()){
                progressBar.visibility=View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                COMPANYUID=companyID.text.toString()
                prefManager.setCompanyID(companyID.text.toString())
                Toast.makeText(this, " Welcome ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
            }
            else{
                progressBar.visibility=View.GONE
                Toast.makeText(this, "This Login credentials are not associated with any employee account", Toast.LENGTH_SHORT).show()
                auth.signOut()
            }

        }.addOnFailureListener {
            progressBar.visibility=View.GONE
            Log.w(TAG, "Error getting documents: ")
        }

    }
}
