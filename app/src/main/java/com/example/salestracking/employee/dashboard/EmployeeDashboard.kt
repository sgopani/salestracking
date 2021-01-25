package com.example.salestracking.employee.dashboard

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.salestracking.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class EmployeeDashboard : Fragment(), View.OnClickListener {
    private lateinit var notes: CardView
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val user = FirebaseAuth.getInstance().currentUser
        rootView= inflater.inflate(R.layout.fragment_employee, container, false)
        notes=rootView.findViewById(R.id.notes)
        notes.setOnClickListener(this)
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.notes -> {
            val action= EmployeeDashboardDirections.actionEmployeeDashboardToNotes2()
            findNavController().navigate(action)
        }
        }
    }
}



//package com.example.salestracking.login
//
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.text.TextUtils
//import android.util.Log
//import android.util.Patterns
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.navigation.findNavController
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.ui.NavigationUI
//import com.example.salestracking.MainActivity
//import com.example.salestracking.R
//import com.example.salestracking.employee.dashboard.EmployeeDashboardDirections
//import com.example.salestracking.register.RegisterAdmin
//import com.google.android.gms.tasks.OnSuccessListener
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.FirebaseFirestore
//
////import com.google.firebase.auth.ktx.auth
//
//class LoginActivity : AppCompatActivity() {
//    private lateinit var emailId:EditText
//    private lateinit var passwordEditText:EditText
//    private lateinit var loginButton:Button
//    private lateinit var auth: FirebaseAuth
//    private lateinit var fstore: FirebaseFirestore
//    private val TAG = "LoginActivity"
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_login)
//        initialize()
//    }
//    private fun initialize(){
//        auth= FirebaseAuth.getInstance()
//        fstore= FirebaseFirestore.getInstance()
//        emailId=findViewById(R.id.email_add_login)
//        passwordEditText=findViewById(R.id.login_password)
//        loginButton=findViewById(R.id.login_button)
//        loginButton.setOnClickListener {
//            //Toast.makeText(this,"Clicked",Toast.LENGTH_SHORT).show()
//            loginUser()
//        }
//        val register=findViewById<Button>(R.id.admin_sign_up)
//        register.setOnClickListener{
//            val intent = Intent(this@LoginActivity, RegisterAdmin::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(intent)
//            finish()
//        }
//    }
//    public override fun onStart() {
//        super.onStart()
//        // Check if user is signed in (non-null) and update UI accordingly.
//        //val currentUser = auth.currentUser
//        //updateUI(currentUser)
//    }
//    private fun loginUser(){
//        val email=emailId.text.toString()
//        val password=passwordEditText.text.toString()
//        if(!Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()||password.length<7){
//            emailId.error="Enter valid details"
//            passwordEditText.error="Enter valid details"
//        }
//        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
//            Log.d(TAG, "Logging in user.")
//            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this) { task ->
//                //mProgressBar!!.hide()
//                if (task.isSuccessful) {
//                    // Sign in success, update UI with signed-in user's information
//                    Log.d(TAG, "signInWithEmail:success")
//                    Toast.makeText(this@LoginActivity, "Authentication success.", Toast.LENGTH_SHORT).show()
//                    checkUserAccessLevel(auth.currentUser?.uid.toString())
//                } else {
//                    // If sign in fails, display a message to the user.
//                    Log.e(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(this@LoginActivity, "Not registered please register", Toast.LENGTH_LONG).show()
//                    val intent = Intent(this@LoginActivity, RegisterAdmin::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
//        else {
//            emailId.error="Field cannot be empty"
//            passwordEditText.error="Field cannot be empty"
//            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
//        }
//    }
//    private fun checkUserAccessLevel(Uid:String) {
//        val df:DocumentReference=fstore.collection("Admin").document(Uid)
//        df.get().addOnSuccessListener { result->
//            Log.d("USER DATA","$result")
//            if(result.getString("isAdmin")!=null){
//                Toast.makeText(this.baseContext,"ADMIN LOGGED IN",Toast.LENGTH_SHORT).show()
//                val action= EmployeeDashboardDirections.actionEmployeeDashboardToAdminDashboard()
//                findNavController().navigate(action)
//            }
//        }
//            .addOnFailureListener(){
//                Toast.makeText(this.baseContext,"ADMIN LOGGED IN",Toast.LENGTH_SHORT).show()
//            }
//
//    }
//}
