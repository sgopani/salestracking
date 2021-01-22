package com.example.salestracking.profile

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import com.example.salestracking.R
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.repository.FireStoreRepository
import java.net.Inet4Address

class ProfileInfo : Fragment() {
    private lateinit var rootView: View
    private  var firebaseRepository = FireStoreRepository()
    private lateinit var name:EditText
    private lateinit var address: EditText
    private lateinit var phoneNo: EditText
    private lateinit var email:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun init() {
        name=rootView.findViewById(R.id.et_profile_name)
        address=rootView.findViewById(R.id.et_profile_address)
        phoneNo=rootView.findViewById(R.id.et_profilePhoneno)
        email=rootView.findViewById(R.id.et_profile_email)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.profile_info, container, false)
        init()
        firebaseRepository.getUserInfo().addOnSuccessListener {document->
            if (document.data!=null) {
                val userInfo = document.toObject(Employee::class.java)
                Log.d("firebaseRepository", "${document.data}")
                name.setText(userInfo?.name)
                address.setText(userInfo?.Address)
                phoneNo.setText(userInfo?.phoneNo)
                email.setText(userInfo?.emailId)
                //Toast.makeText(this.requireContext(),"${userInfo?.phoneNo}",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(this.requireContext(),"Failed",Toast.LENGTH_SHORT).show()
        }
        return rootView
    }

}