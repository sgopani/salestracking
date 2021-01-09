package com.example.salesadmin.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.salesadmin.MainActivity
import com.example.salesadmin.R
import com.example.salesadmin.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

private lateinit var auth: FirebaseAuth
private lateinit var fstore:FirebaseFirestore
private lateinit var name: EditText
private lateinit var email:EditText
private lateinit var password:EditText
private lateinit var phoneNumber:EditText
private lateinit var address:EditText
private lateinit var signUpButton: Button
private lateinit var registerLogin:Button
private var valid:Boolean=true
private lateinit var progressBar: ProgressBar
class RegisterAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_admin)
        name=findViewById<EditText>(R.id.et_product_name)
        email=findViewById<EditText>(R.id.signUp_email)
        password=findViewById<EditText>(R.id.SignUp_Password)
        phoneNumber=findViewById<EditText>(R.id.signUp_phoneno)
        address=findViewById<EditText>(R.id.signUp_address)
        signUpButton=findViewById<Button>(R.id.signUp_button)
        registerLogin=findViewById<Button>(R.id.login_register_button)
        auth= FirebaseAuth.getInstance()
        fstore= FirebaseFirestore.getInstance()
        signUpButton.setOnClickListener{
            registerAdmin()
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
            createUser()
        }
        else{
            Toast.makeText(this,"Please enter fields properly",Toast.LENGTH_SHORT).show()
        }

    }
    private fun createUser(){
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this){task->
            if (task.isSuccessful){
                val user = auth.currentUser
                val df: DocumentReference = fstore.collection("{${user?.uid}}").document("Admin").collection("${user?.email}").document("Information")
                val userInfo= mutableMapOf<String,String>()
                userInfo["name"] = name.text.toString()
                userInfo["email Id"] = email.text.toString()
                userInfo["Phone no"]= phoneNumber.text.toString()
                userInfo["Address"]= address.text.toString()
                userInfo["isAdmin"]="1"
                userInfo["location"]="15632162132165"
                Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
                df.set(userInfo).addOnSuccessListener {
                    Log.d("Database","Inserted Successfully")
                }
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)

            }
            else{
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