package com.example.salestracking.employee.dashboard

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.salestracking.*
import com.example.salestracking.databse.model.Attendance
import com.example.salestracking.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class EmployeeDashboard : Fragment(), View.OnClickListener {
    private lateinit var notes: CardView
    private lateinit var applyLeave:CardView
    private lateinit var collections:CardView
    private lateinit var orders:CardView
    private lateinit var attendence:CardView
    private lateinit var rootView: View
    private lateinit var checkInBtn:Button
    private lateinit var checkOutBtn:Button
    private  var isCheckIn=false
    private  var checkInDate:String=""
    private lateinit var prefManager:PrefManager
    private lateinit var viewModel: FireStoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
//    tools:context=".products.ProductList">
     private fun init(){
         notes=rootView.findViewById(R.id.notes)
         applyLeave=rootView.findViewById(R.id.cv_apply_leave)
         collections=rootView.findViewById(R.id.cv_collections)
         orders=rootView.findViewById(R.id.cv_take_orders)
         checkInBtn=rootView.findViewById(R.id.btn_check_in)
         checkOutBtn=rootView.findViewById(R.id.btn_check_out)
         attendence=rootView.findViewById(R.id.cv_attendence)
         prefManager=PrefManager(this.requireContext())
         viewModel= FireStoreViewModel()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //val user = FirebaseAuth.getInstance().currentUser
        rootView= inflater.inflate(R.layout.fragment_employee, container, false)
        init()
        collections.setOnClickListener(this)
        applyLeave.setOnClickListener(this)
        notes.setOnClickListener(this)
        orders.setOnClickListener(this)
        checkInBtn.setOnClickListener(this)
        checkOutBtn.setOnClickListener(this)
        isCheckIn=prefManager.getIsCheckedIn()
        attendence.setOnClickListener(this)
        if(isCheckIn){
            //checkOutBtn.visibility=View.VISIBLE
            //checkInBtn.visibility=View.GONE
        }
        else{
            //checkInBtn.visibility=View.VISIBLE
            //checkOutBtn.visibility=View.GONE
        }
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.notes -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToNotes2()
                findNavController().navigate(action)
            }
            R.id.cv_apply_leave -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToLeaveList()
                findNavController().navigate(action)
            }
            R.id.cv_collections -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToCollectionList()
                findNavController().navigate(action)
            }
            R.id.cv_take_orders -> {
//                val action = EmployeeDashboardDirections.actionEmployeeDashboardToAddOrders()
//                findNavController().navigate(action)
                requestCode =0
                val tag="String"
                val action=EmployeeDashboardDirections.actionEmployeeDashboardToPartyList()
                findNavController().navigate(action)
            }
            R.id.btn_check_in -> {
                //markAttendance()
            }
            R.id.btn_check_out -> {
                //prefManager.setIsCheckedIn(false)
                //checkInBtn.visibility=View.VISIBLE
                //checkOutBtn.visibility=View.GONE
            }
            R.id.cv_attendence -> {
                markAttendance()
            }
        }
    }
    private fun markAttendance() {
        val valid=prefManager.getIsCheckedIn()
        val fstore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
                .collection("employee")
                .document("${user?.email}").collection("Attendance")
                .document(toSimpleDateFormat(System.currentTimeMillis()))
        df.get().addOnSuccessListener { document ->
            if (document.exists()) {
                prefManager.setIsCheckedIn(false)
                AlertDialog.Builder(context).apply {
                    setTitle("Attendance Marked")
                    setMessage("You have already marked attendance for the day")
                    setPositiveButton("OK") { _, _ ->
                    }
                }.create().show()
            }else{
                prefManager.setIsCheckedIn(true)
                if (valid) {
                    AlertDialog.Builder(context).apply {
                        setTitle("Are you sure you want to mark your attendance?")
                        setMessage("You can mark only once in a day")
                        setPositiveButton("Yes") { _, _ ->
                            val date = toSimpleDateFormat(System.currentTimeMillis())
                            val checkIn = Calendar.getInstance().time.toString()
                            val attendance = Attendance(
                                user!!.uid,
                                System.currentTimeMillis(),
                                date,
                                checkIn
                            )
                            viewModel.addAttendanceFirebase(attendance)
                            checkInDate = date
                            //prefManager.setIsCheckedIn(true)
                            //checkOutBtn.visibility = View.VISIBLE
                            //checkInBtn.visibility = View.INVISIBLE
                        }
                        setNegativeButton("No") { _, _ ->

                        }
                    }.create().show()
                }

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
