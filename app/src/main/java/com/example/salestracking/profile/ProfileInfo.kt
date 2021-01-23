package com.example.salestracking.profile

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.salestracking.PrefManager
import com.example.salestracking.R
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.login.LoginActivity
import com.example.salestracking.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseAuth
import java.net.Inet4Address

class ProfileInfo : Fragment() {
    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth
    private  var firebaseRepository = FireStoreRepository()
    private lateinit var name:EditText
    private lateinit var address: EditText
    private lateinit var phoneNo: EditText
    private lateinit var email:EditText
    private lateinit var prefManager: PrefManager
    private lateinit var tv_name:TextView
    private lateinit var signOut:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init() {
        name=rootView.findViewById(R.id.et_profile_name)
        address=rootView.findViewById(R.id.et_profile_address)
        phoneNo=rootView.findViewById(R.id.et_profilePhoneno)
        email=rootView.findViewById(R.id.et_profile_email)
        prefManager = PrefManager(this.requireContext())
        tv_name=rootView.findViewById(R.id.tv_name)
        signOut=rootView.findViewById(R.id.sign_Out)
        firebaseAuth= FirebaseAuth.getInstance()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.profile_info, container, false)
        init()
        tv_name.text = prefManager.getFullName()
        name.setText(prefManager.getFullName())
        address.setText(prefManager.getAddress())
        phoneNo.setText(prefManager.getPhoneNo())
        email.setText(prefManager.getEmail())
        firebaseRepository.getUserInfo().addOnSuccessListener {document->
            if (document.data!=null) {
                val userInfo = document.toObject(Employee::class.java)
                Log.d("firebaseRepository", "${document.data}")
                prefManager.setFullName(userInfo?.name.toString())
                prefManager.setAddress(userInfo?.Address.toString())
                prefManager.setEmail(userInfo?.emailId.toString())
                prefManager.setPhone(userInfo?.phoneNo.toString())
                name.setText(prefManager.getFullName())
                address.setText(prefManager.getAddress())
                phoneNo.setText(prefManager.getPhoneNo())
                email.setText(prefManager.getEmail())
            }
        }.addOnFailureListener {
            Toast.makeText(this.requireContext(),"Failed",Toast.LENGTH_SHORT).show()
        }

        signOut.setOnClickListener {
            firebaseAuth.signOut()
            val intent= Intent(this.requireActivity(), LoginActivity::class.java)
            startActivity(intent)
            this.activity?.finish()
        }

        return rootView
    }


}