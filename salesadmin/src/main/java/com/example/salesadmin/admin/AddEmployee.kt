//package com.example.salesadmin.admin
//
//import android.os.Bundle
//import android.util.Log
//import android.util.Patterns
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.navigation.fragment.findNavController
//import com.example.salesadmin.R
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firestore.v1.DocumentTransform.FieldTransform.ServerValue
//
//
//class AddEmployee : Fragment() {
//    private lateinit var auth: FirebaseAuth
//    private lateinit var fstore: FirebaseFirestore
//    private lateinit var name: EditText
//    private lateinit var email: EditText
//    private lateinit var password: EditText
//    private lateinit var phoneNumber: EditText
//    private lateinit var confirmPassword:EditText
//    private lateinit var address: EditText
//
//    private lateinit var signUpButton: Button
//    private lateinit var user:FirebaseUser
//    //private lateinit var cancel: Button
//    private var valid:Boolean=true
//    private lateinit var rootView: View
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//    }
//    fun init(){
//        name=rootView.findViewById(R.id.et_employee_name)
//        email=rootView.findViewById(R.id.tv_add_emailAddress)
//        phoneNumber=rootView.findViewById(R.id.et_add_phoneNumber)
//        password=rootView.findViewById(R.id.tv_add_Password)
//        confirmPassword=rootView.findViewById(R.id.tv_confirm_password)
//        address=rootView.findViewById(R.id.tv_add_address)
//        signUpButton=rootView.findViewById(R.id.add_employee_button)
//        val user = FirebaseAuth.getInstance().currentUser
//        fstore= FirebaseFirestore.getInstance()
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        rootView=inflater.inflate(R.layout.add_employee, container, false)
//        init()
//
//        signUpButton.setOnClickListener{
//            addEmployee()
//        }
//        return rootView
//    }
//
//    private fun addEmployee() {
//        Log.d("password", "${password.text.toString() != confirmPassword.text.toString()}")
//        checkField(name)
//        checkField(email)
//        checkField(address)
//        checkField(password)
//        checkField(confirmPassword)
//        checkField(phoneNumber)
//        if (password.text.toString() != confirmPassword.text.toString()) {
//            password.error="Both Password should be same"
//            confirmPassword.error="Both Password should be same"
//            valid=false
//        }
//        else{
//        if(valid){
//            registerEmployee()
//            //Toast.makeText(this.requireContext(),"Valid",Toast.LENGTH_SHORT).show()
//        }
//            else{
//            Toast.makeText(this.requireContext(), "Please enter every field correctly", Toast.LENGTH_SHORT).show()
//        }
//        }
////        else{
////            Toast.makeText(this.requireContext(),"InValid",Toast.LENGTH_SHORT).show()
////        }
//    }
//
//    private fun registerEmployee() {
//        user= FirebaseAuth.getInstance().currentUser!!
//        auth= FirebaseAuth.getInstance()
//        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener { task->
//            if (task.isSuccessful){
//                val timestamp: Any = ServerValue.REQUEST_TIME
//                val df: DocumentReference = fstore.collection(user.uid).document("employee").collection("${user!!.email}").document("Information")
//                val userInfo= mutableMapOf<String, String>()
//                userInfo["name"] = name.text.toString()
//                userInfo["email Id"] = email.text.toString()
//                userInfo["Phone no"]= phoneNumber.text.toString()
//                userInfo["Address"]= address.text.toString()
//                userInfo["Date And time"]=timestamp.toString()
//                userInfo["Uid"]= user!!.uid
//                userInfo["isEmployee"]="1"
//                df.set(userInfo).addOnSuccessListener {
//                    Toast.makeText(this.requireContext()," Added Successful",Toast.LENGTH_SHORT).show()
//                    val action=AddEmployeeDirections.actionAddEmployeeToAdminDashboard()
//                    findNavController().navigate(action)
//                }.addOnFailureListener {
//                    Toast.makeText(this.requireContext()," Unable to add",Toast.LENGTH_SHORT).show()
//                }
//            }
//            else{
//                Toast.makeText(this.requireContext(),"Employee Already exists",Toast.LENGTH_SHORT).show()
//            }
//
//        }
//    }
//
////    private fun checkField(textField: EditText): Boolean {
////        when {
////            textField.text.toString().isEmpty() -> {
////                textField.error = "Field cannot be empty"
////                valid = false
////            }
////            address.text.toString().length<10->{
////                address.error="Enter proper address"
////            }
////            password.text.toString().length<7 -> {
////                password.error="Minimum 6 character password required"
////                valid = false
////            }
////            !Patterns.EMAIL_ADDRESS.matcher(email.text.toString().trim()).matches() -> {
////                email.error="Please enter valid email address"
////                email.requestFocus()
////                valid = false
////            }
////            Patterns.PHONE.matcher(phoneNumber.text.toString()).matches() -> {
////                if(phoneNumber.text.length==10){
////                    valid=true
////                }
////                else {
////                    phoneNumber.error = "Please correct mobile number"
////                    valid = false
////                }
////            }
////            else -> {
////                valid = true
////            }
////        }
////        return valid
////    }
//
//}