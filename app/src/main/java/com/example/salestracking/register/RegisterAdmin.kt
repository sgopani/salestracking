//package com.example.salestracking.register
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.util.Patterns
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ProgressBar
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import com.example.salestracking.MainActivity
//import com.example.salestracking.R
//import com.example.salestracking.login.LoginActivity
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.FirebaseFirestore
//
//private lateinit var auth:FirebaseAuth
//private lateinit var fstore:FirebaseFirestore
//private lateinit var name:EditText
//private lateinit var email:EditText
//private lateinit var password:EditText
//private lateinit var phoneNumber:EditText
//private lateinit var address:EditText
//private lateinit var signUpButton:Button
//private lateinit var registerLogin:Button
//private var valid:Boolean=true
//private lateinit var progressBar: ProgressBar
//class RegisterAdmin : AppCompatActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_register_admin)
//        name=findViewById<EditText>(R.id.admin_name)
//        email=findViewById<EditText>(R.id.signUp_email)
//        password=findViewById<EditText>(R.id.SignUp_Password)
//        phoneNumber=findViewById<EditText>(R.id.signUp_phoneno)
//        address=findViewById<EditText>(R.id.signUp_address)
//        signUpButton=findViewById<Button>(R.id.signUp_button)
//        registerLogin=findViewById<Button>(R.id.login_register_button)
//        auth= FirebaseAuth.getInstance()
//        fstore= FirebaseFirestore.getInstance()
//        signUpButton.setOnClickListener{
//            registerAdmin()
//        }
//        registerLogin.setOnClickListener {
//            val intent = Intent(this, LoginActivity::class.java)
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//            startActivity(intent)
//            finish()
//        }
//
//    }
//
//    private fun registerAdmin() {
//        checkField(name)
//        checkField(email)
//        checkField(password)
//        checkField(phoneNumber)
//        checkField(address)
//        if (valid){
//            createUser()
//        }
//        else{
//            Toast.makeText(this,"Please enter fields properly",Toast.LENGTH_SHORT).show()
//        }
//
//    }
//    private fun createUser(){
//        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this){task->
//            if (task.isSuccessful){
//                val user = auth.currentUser
//                val df:DocumentReference= fstore.collection("Admin").document(user?.uid!!)
//                val userInfo= mutableMapOf<String,String>()
//                userInfo["name"] = name.text.toString()
//                userInfo["email Id"] = email.text.toString()
//                userInfo["Phone no"]= phoneNumber.text.toString()
//                userInfo["Address"]= address.text.toString()
//                userInfo["isAdmin"]="1"
//                Toast.makeText(this,"Successful",Toast.LENGTH_SHORT).show()
//                df.set(userInfo).addOnSuccessListener {
//                    Log.d("Database","Inserted Successfully")
//                }
//                val intent = Intent(this, LoginActivity::class.java)
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//                startActivity(intent)
//
//            }
//            else{
//                Toast.makeText(baseContext, "User Already exists",
//                    Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//            }
//        }
//    }
//
//
//    private fun checkField(textField: EditText): Boolean {
//        when {
//            textField.text.toString().isEmpty() -> {
//                textField.error = "Field cannot be empty"
//                valid = false
//            }
//            password.text.toString().length<7 -> {
//                password.error="Minimum 6 character password required"
//                valid = false
//            }
//            !Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim()).matches() -> {
//                email.error="Please enter valid email address"
//                email.requestFocus()
//                valid = false
//            }
//            Patterns.PHONE.matcher(phoneNumber.text.toString()).matches() -> {
//                if(phoneNumber.text.length==10){
//                    valid=true
//                }
//                else {
//                    phoneNumber.error = "Please correct mobile number"
//                    valid = false
//                }
//            }
//            else -> {
//                valid = true
//            }
//        }
//        return valid
//    }
//}
//
//
////<?xml version="1.0" encoding="utf-8"?>
////<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
////xmlns:app="http://schemas.android.com/apk/res-auto"
////xmlns:tools="http://schemas.android.com/tools"
////android:layout_width="match_parent"
////android:layout_height="match_parent">
////
////<androidx.constraintlayout.widget.ConstraintLayout
////android:id="@+id/constraintLayout"
////android:layout_width="0dp"
////android:layout_height="0dp"
////app:layout_constraintBottom_toBottomOf="parent"
////app:layout_constraintEnd_toEndOf="parent"
////app:layout_constraintStart_toStartOf="parent"
////app:layout_constraintTop_toBottomOf="@+id/RelativeLayout">
////
////<GridLayout
////android:id="@+id/gridLayout"
////android:layout_width="match_parent"
////android:layout_height="match_parent"
////android:layout_gravity="center_horizontal"
////android:layout_margin="8dp"
////android:alignmentMode="alignMargins"
////android:columnCount="2"
////android:columnOrderPreserved="false"
////android:rowCount="4"
////tools:layout_editor_absoluteX="34dp"
////tools:layout_editor_absoluteY="5dp">
////
////<androidx.cardview.widget.CardView
////android:layout_width="170dp"
////android:layout_height="170dp"
////android:layout_margin="10dp"
////app:cardCornerRadius="12dp"
////app:cardElevation="5dp">
////
////<LinearLayout
////android:layout_width="match_parent"
////android:layout_height="match_parent"
////android:gravity="center"
////android:orientation="vertical">
////
////<ImageView
////android:id="@+id/imageView4"
////android:layout_width="match_parent"
////android:layout_height="wrap_content"
////tools:srcCompat="@tools:sample/avatars" />
////
////<TextView
////android:id="@+id/textView6"
////android:layout_width="wrap_content"
////android:layout_height="match_parent"
////android:text="TextView" />
////</LinearLayout>
////
////</androidx.cardview.widget.CardView>
////
////<androidx.cardview.widget.CardView
////android:layout_width="170dp"
////android:layout_height="170dp"
////android:layout_margin="10dp"
////app:cardCornerRadius="12dp"
////app:cardElevation="5dp">
////
////<LinearLayout
////android:layout_width="match_parent"
////android:layout_height="match_parent"
////android:gravity="center"
////android:orientation="vertical">
////
////<ImageView
////android:id="@+id/imageView5"
////android:layout_width="match_parent"
////android:layout_height="wrap_content"
////tools:srcCompat="@tools:sample/avatars" />
////
////<TextView
////android:id="@+id/textView7"
////android:layout_width="wrap_content"
////android:layout_height="match_parent"
////android:text="TextView" />
////</LinearLayout>
////
////</androidx.cardview.widget.CardView>
////
////<androidx.cardview.widget.CardView
////android:layout_width="170dp"
////android:layout_height="170dp"
////android:layout_margin="10dp"
////app:cardCornerRadius="12dp"
////app:cardElevation="5dp">
////
////<LinearLayout
////android:layout_width="match_parent"
////android:layout_height="match_parent"
////android:gravity="center"
////android:orientation="vertical">
////
////<ImageView
////android:id="@+id/imageView6"
////android:layout_width="match_parent"
////android:layout_height="wrap_content"
////tools:srcCompat="@tools:sample/avatars" />
////
////<TextView
////android:id="@+id/textView8"
////android:layout_width="wrap_content"
////android:layout_height="match_parent"
////android:text="TextView" />
////</LinearLayout>
////
////</androidx.cardview.widget.CardView>
////</GridLayout>
////
////</androidx.constraintlayout.widget.ConstraintLayout>
////
////<RelativeLayout
////android:id="@+id/RelativeLayout"
////android:layout_width="0dp"
////android:layout_height="136dp"
////android:background="@drawable/btn_custom"
////android:orientation="vertical"
////app:layout_constraintEnd_toEndOf="parent"
////app:layout_constraintStart_toStartOf="parent"
////app:layout_constraintTop_toTopOf="parent">
////
////<TextView
////android:id="@+id/textView3"
////android:layout_width="251dp"
////android:layout_height="99dp"
////android:layout_alignParentEnd="true"
////android:layout_alignParentRight="true"
////android:layout_marginEnd="161dp"
////android:layout_marginRight="161dp"
////android:text="User Name" />
////</RelativeLayout>
////</androidx.constraintlayout.widget.ConstraintLayout>