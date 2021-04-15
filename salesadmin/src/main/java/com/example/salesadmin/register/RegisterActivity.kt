package com.example.salesadmin.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.salesadmin.MainActivity
import com.example.salesadmin.R
import com.example.salesadmin.isInternetOn
import com.example.salesadmin.login.LoginActivity
import com.example.salesadmin.model.RegisterAdmin
import com.example.salesadmin.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

    private lateinit var auth: FirebaseAuth
    private lateinit var name: EditText
    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var phoneNumber:EditText
    private lateinit var address:EditText
    private lateinit var signUpButton: Button
    private lateinit var registerLogin:TextView
    private var valid:Boolean=true
    private lateinit var viewModel:FireStoreViewModel
    private lateinit var progressBar: ProgressBar
    class RegisterActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_register_admin)
            name=findViewById<EditText>(R.id.et_admin_name)
            email=findViewById<EditText>(R.id.signUp_email)
            password=findViewById<EditText>(R.id.SignUp_Password)
            phoneNumber=findViewById<EditText>(R.id.signUp_phoneno)
            address=findViewById<EditText>(R.id.signUp_address)
            signUpButton=findViewById<Button>(R.id.signUp_button)
            registerLogin=findViewById<TextView>(R.id.login_link)
            progressBar=findViewById(R.id.progress_bar)
            auth= FirebaseAuth.getInstance()

            signUpButton.setOnClickListener{
                if(isInternetOn(this)){
                    signUpButton.isClickable=false
                    registerAdmin()
                }
                else{
                    Toast.makeText(this,"Please check your internet connection ",Toast.LENGTH_SHORT).show()
                    signUpButton.isClickable=true
                }

            }
            registerLogin.setOnClickListener {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
            }
        }

        private fun registerAdmin() {
            checkField(name)
            checkField(email)
            checkField(password)
            checkField(phoneNumber)
            checkField(address)
            if (valid){
                progressBar.visibility= View.VISIBLE
                createUser()
            }
            else{
                signUpButton.isClickable=true
                Toast.makeText(this,"Please enter fields properly",Toast.LENGTH_SHORT).show()
            }
        }
        private fun createUser(){
            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    viewModel= FireStoreViewModel()
                    //val user = auth.currentUser
                    val registerAdmin=RegisterAdmin(name.text.toString(),email.text.toString(),phoneNumber.text.toString()
                            ,address.text.toString(),"1", auth.currentUser?.uid!!)
                    viewModel.registerAdminFirebase(registerAdmin)
                    Toast.makeText(this," Registered successfully",Toast.LENGTH_SHORT).show()
                    progressBar.visibility= View.GONE
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)

                }
                else{
                    signUpButton.isClickable=true
                    progressBar.visibility= View.GONE
                    Toast.makeText(baseContext, "User Already exists",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }


        private fun checkField(textField: EditText): Boolean {
            when {
                textField.text.toString().isEmpty() -> {
                    textField.error = "Field cannot be empty"
                    valid = false
                }
                password.text.toString().length<7 -> {
                    password.error="Minimum 6 character password required"
                    valid = false
                }
                !Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim()).matches() -> {
                    email.error="Please enter valid email address"
                    email.requestFocus()
                    valid = false
                }
                Patterns.PHONE.matcher(phoneNumber.text.toString()).matches() -> {
                    if(phoneNumber.text.length==10){
                        valid=true
                    }
                    else {
                        phoneNumber.error = "Please correct mobile number"
                        valid = false
                    }
                }

                else -> {
                    valid = true
                }
            }
            return valid
        }
    }