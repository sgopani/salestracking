package com.example.salesadmin.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.salesadmin.MainActivity
import com.example.salesadmin.R
import com.example.salesadmin.register.RegisterActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore


//import com.google.firebase.auth.ktx.auth

class LoginActivity : AppCompatActivity() {
    private lateinit var emailId: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private lateinit var user:FirebaseUser
    private lateinit var forgotPassword: TextView
    private lateinit var progressBar: ProgressBar
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
        progressBar=findViewById(R.id.progress_bar)
        forgotPassword=findViewById(R.id.forgot_password_tv)
        fstore= FirebaseFirestore.getInstance()
        loginButton.setOnClickListener {
            //Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
            loginButton.isClickable=false
            loginUser()
        }
        val register = findViewById<TextView>(R.id.admin_sign_up)
        register.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
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
        // Check if user is signed in (non-null) and update UI accordingly.
        //val currentUser = auth.currentUser
        //updateUI(currentUser)
    }

    private fun loginUser() {
        val email = emailId.text.toString()
        val password = passwordEditText.text.toString()
        if (!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() || password.length < 7) {
            emailId.error = "Enter valid details"
            passwordEditText.error = "Enter valid details"
            loginButton.isClickable=true
            return
        }
        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            Log.d(TAG, "Logging in user.")
            progressBar.visibility= View.VISIBLE
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                //mProgressBar!!.hide()
                if (task.isSuccessful) {
                    updateUI()
                    //}
                }
            }.addOnFailureListener(OnFailureListener { exeption->
                //Log.d("exeption", exeption.localizedMessage)
                loginButton.isClickable=true
                when {
                    exeption.localizedMessage=="The password is invalid or the user does not have a password." -> {
                        Toast.makeText(this, "Enter Valid Password",Toast.LENGTH_SHORT).show()
                    }
                    exeption.localizedMessage=="There is no user record corresponding to this identifier. The user may have been deleted." -> {
                        Toast.makeText(this, "User Doesn't exist contact admin",Toast.LENGTH_SHORT).show()
                    }
                    exeption== FirebaseAuthInvalidCredentialsException(email,password) -> {
                        Toast.makeText(this, "Enter Valid Credentials",Toast.LENGTH_SHORT).show()
                    }
                    exeption== FirebaseAuthUserCollisionException (email,password) -> {
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
            loginButton.isClickable=true
            progressBar.visibility=View.GONE
            emailId.error="Field cannot be empty"
            passwordEditText.error="Field cannot be empty"
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateUI() {
        loginButton.isClickable=false
        user= auth.currentUser!!
        val documentReference=fstore.collection("Sales").document(user.uid)
                .collection("admin").document("Admin Info")
        documentReference.get().addOnSuccessListener {document->
            if(document.exists()){
                loginButton.isClickable=false
                progressBar.visibility=View.GONE
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(this, " Welcome ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()
            }
            else{
                loginButton.isClickable=true
                progressBar.visibility=View.GONE
                Toast.makeText(this, "This Login credentials are not associated with any admin account", Toast.LENGTH_SHORT).show()
                auth.signOut()
            }
        }.addOnFailureListener {
            progressBar.visibility=View.GONE
            Log.w(TAG, "Error getting documents: ")
        }

    }
}
