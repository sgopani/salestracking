package com.example.salestracking.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salestracking.*
import com.example.salestracking.databse.SalesDatabase
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.employee.notes.NotesViewModel
import com.example.salestracking.login.LoginActivity
import com.example.salestracking.repository.FireStoreRepository
import com.example.salestracking.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.net.Inet4Address
import java.util.*

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
    private lateinit var updateBtn:Button
    private lateinit var edit:TextView
    private lateinit var dateOfJoining:EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var profileImageView: ImageView
    private lateinit var drawable: TextDrawable
    private lateinit var viewModel: NotesViewModel
    private lateinit var salesDatabase: SalesDatabase

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
        updateBtn=rootView.findViewById(R.id.update_profile_btn)
        firebaseAuth= FirebaseAuth.getInstance()
        progressBar = rootView.findViewById(R.id.progress_bar)
        edit=rootView.findViewById(R.id.tv_edit_profile)
        dateOfJoining=rootView.findViewById(R.id.et_date_of_joining)
        profileImageView=rootView.findViewById(R.id.iv_profile_image)
        salesDatabase= SalesDatabase.getInstance(this.requireContext())
        viewModel= NotesViewModel(salesDatabase)
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
        dateOfJoining.setText(toSimpleDateFormat(prefManager.getDOJ()!!))
        //if(prefManager.getFullName()?.isEmpty()!!){
        firebaseRepository.getUserInfo().addOnSuccessListener {document->
            if (document.data!=null) {
                val userInfo = document.toObject(Employee::class.java)
                Log.d("firebaseRepository", "${document.data}")
                prefManager.setFullName(userInfo?.name.toString())
                prefManager.setAddress(userInfo?.Address.toString())
                prefManager.setEmail(userInfo?.emailId.toString())
                prefManager.setPhone(userInfo?.phoneNo.toString())
                prefManager.setDOJ(userInfo?.time!!.toLong())
                name.setText(prefManager.getFullName())
                address.setText(prefManager.getAddress())
                phoneNo.setText(prefManager.getPhoneNo())
                email.setText(prefManager.getEmail())
                tv_name.text = prefManager.getFullName()
                dateOfJoining.setText(toSimpleDateFormat(prefManager.getDOJ()!!))

            }
        }.addOnFailureListener {
            Toast.makeText(this.requireContext(),"Failed",Toast.LENGTH_SHORT).show()
        }
        //}

        val generator: ColorGenerator = ColorGenerator.MATERIAL
        val color: Int = generator.randomColor
        drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
            .buildRound(name.text[0].toString().toUpperCase(Locale.ROOT), color)
        profileImageView.setImageDrawable(drawable)

        signOut.setOnClickListener {
            AlertDialog.Builder(context).apply {
                setTitle("Are you sure you want to want to sign out")
                setMessage("You cannot undo this operation")
                setPositiveButton("Yes") { _, _ ->
                    signOut()
                }
                setNegativeButton("No") { _, _ ->

                }
            }.create().show()
        }
        edit.setOnClickListener {
            name.isEnabled=true
            address.isEnabled=true
            edit.visibility=View.GONE
            updateBtn.visibility=View.VISIBLE
        }
        updateBtn.setOnClickListener {
            if(isInternetOn(this.requireActivity())){
            if(name.text.isNotEmpty() && address.text.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                val fstore = FirebaseFirestore.getInstance()
                val user = FirebaseAuth.getInstance().currentUser
                val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
                        .collection("employee").document("${user?.email}")
                df.update("name", name.text.toString(), "address", address.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressBar.visibility = View.GONE
                                updateBtn.visibility = View.GONE
                                edit.visibility = View.VISIBLE
                                name.isEnabled=false
                                address.isEnabled=false
                                Toast.makeText(this.requireContext(), "Updated Successfully", Toast.LENGTH_SHORT).show()
                            } else {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this.requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
            }
            else{
                Toast.makeText(this.requireContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }else{
                Toast.makeText(this.requireContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        return rootView

    }
    private  fun signOut(){

        if(isInternetOn(this.requireActivity())){
            try{
                firebaseAuth.signOut()
                val intent= Intent(this.requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                this.activity?.finish()
                prefManager.clear()
                viewModel.deleteAll()
            }

            catch (e:Exception){
                Toast.makeText(this.requireContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show()
            }

        }
    }


}