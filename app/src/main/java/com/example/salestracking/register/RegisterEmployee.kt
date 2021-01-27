package com.example.salestracking.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.salestracking.MainActivity
import com.example.salestracking.R
import com.example.salestracking.login.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.example.salestracking.COMPANYUID
import com.example.salestracking.PrefManager
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.repository.FireStoreViewModel

class RegisterEmployee : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var fstore: FirebaseFirestore
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var address: EditText
    private lateinit var companyId: EditText
    private lateinit var signUpButton: Button
    private lateinit var user: FirebaseAuth
    private lateinit var progressBar: ProgressBar
    private lateinit var viewModel: FireStoreViewModel
    private var valid: Boolean = true
    private lateinit var loginLink: TextView
    private lateinit var uid:String
    private lateinit var prefManager: PrefManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_employee)
        init()
//        rootView = inflater.inflate(R.layout.register_employee, container, false)
//        return rootView
    }

    private fun init() {
        name = findViewById(R.id.et_employee_name)
        email = findViewById(R.id.tv_add_emailAddress)
        phoneNumber = findViewById(R.id.et_add_phoneNumber)
        password = findViewById(R.id.tv_add_Password)
        confirmPassword =findViewById(R.id.tv_confirm_password)
        address = findViewById(R.id.tv_add_address)
        signUpButton= findViewById(R.id.add_employee_button)
        companyId = findViewById(R.id.company_id)
        loginLink=findViewById(R.id.loginLink)
        progressBar=findViewById(R.id.progress_bar)
        fstore = FirebaseFirestore.getInstance()
        signUpButton.setOnClickListener{
            addEmployee()
        }
        loginLink.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        prefManager = PrefManager(this)
    }


    private fun addEmployee() {
        Log.d("password", "${password.text.toString() != confirmPassword.text.toString()}")
        checkField(name)
        checkField(email)
        checkField(address)
        checkField(password)
        checkField(confirmPassword)
        checkField(phoneNumber)
        checkField(companyId)
        if (password.text.toString() != confirmPassword.text.toString()) {
            password.error = "Both Password should be same"
            confirmPassword.error = "Both Password should be same"
            valid = false
        } else {
            if (valid) {
                progressBar.visibility= View.VISIBLE
                checkIfEmployeeExist()
                //Toast.makeText(this,"${companyId.text}",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter every field correctly", Toast.LENGTH_SHORT)
                    .show()
            }
        }
//        else{
//            Toast.makeText(this.requireContext(),"InValid",Toast.LENGTH_SHORT).show()
//        }
    }

    private fun checkField(textField: EditText): Boolean {
        when {
            textField.text.toString().isEmpty() -> {
                textField.error = "Field cannot be empty"
                valid = false
            }
            address.text.toString().length < 10 -> {
                address.error = "Enter proper address"
                valid=false
            }
            password.text.toString().length < 7 -> {
                password.error = "Minimum 6 character password required"
                valid = false
            }
            !Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim()).matches() -> {
                email.error = "Please enter valid email address"
                email.requestFocus()
                valid = false
            }
            Patterns.PHONE.matcher(phoneNumber.text.toString()).matches() -> {
                if (phoneNumber.text.length == 10) {
                    valid = true
                } else {
                    phoneNumber.error = "Please enter correct mobile number"
                    valid = false
                }
            }
            else -> {
                valid = true
            }
        }
        return valid
    }

    private fun checkIfEmployeeExist() {
        uid=companyId.text.toString()
        COMPANYUID=uid.toString()
        val documentReference=fstore
                .collection("Sales")
                .document("$uid").collection("admin")
                .document("Admin Info")
                documentReference.get().addOnSuccessListener {document->
                    if(document.exists()) {
                       registerEmployee()
                    }
                    else{
                        progressBar.visibility= View.GONE
                        Toast.makeText(this,"No company registered with this Id",Toast.LENGTH_LONG).show()
                    }
                }
    }
    private fun registerEmployee() {
        user = FirebaseAuth.getInstance()
        auth = FirebaseAuth.getInstance()
        fstore=FirebaseFirestore.getInstance()
        val time=System.currentTimeMillis()
        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        viewModel= FireStoreViewModel()
                        progressBar.visibility= View.GONE
                        val employee=Employee(name.text.toString(),email.text.toString(),phoneNumber.text.toString(),address.text.toString()
                        , auth.currentUser!!.uid,"1",companyId.text.toString(),time.toString())
                        prefManager.setCompanyID(companyId.text.toString())
                        viewModel.registerAdminFirebase(employee)
                        Toast.makeText(
                                    this,
                                    "Registered Successfully",
                                    Toast.LENGTH_SHORT
                            ).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

//                        }.addOnFailureListener {
//                            Toast.makeText(this, "Unable to add", Toast.LENGTH_SHORT)
//                                    .show()
//                        }
                    } else {
                        progressBar.visibility= View.GONE
                        Toast.makeText(
                                this,
                                "Employee Already exists",
                                Toast.LENGTH_SHORT
                        ).show()
                    }

                }
    }
}
