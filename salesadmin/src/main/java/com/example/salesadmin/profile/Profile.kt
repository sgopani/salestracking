package com.example.salesadmin.profile

import android.app.AlertDialog
import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salesadmin.PrefManager
import com.example.salesadmin.R
import com.example.salesadmin.isInternetOn
import com.example.salesadmin.login.LoginActivity
import com.example.salesadmin.model.Employee
import com.example.salesadmin.repository.FireStoreRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class Profile : Fragment() {
    private lateinit var rootView: View
    private lateinit var firebaseAuth: FirebaseAuth
    private  var firebaseRepository = FireStoreRepository()
    private lateinit var name: EditText
    private lateinit var address: EditText
    private lateinit var phoneNo: EditText
    private lateinit var email: EditText
    private lateinit var prefManager: PrefManager
    private lateinit var tv_name: TextView
    private lateinit var signOut: Button
    private lateinit var updateBtn: Button
    private lateinit var edit: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var profileImageView: ImageView
    private lateinit var share_unique_id: ImageView
    private lateinit var drawable: TextDrawable
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
        profileImageView=rootView.findViewById(R.id.iv_profile_image)
        share_unique_id=rootView.findViewById(R.id.share_unique_id)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.profile, container, false)
        init()
        tv_name.text = prefManager.getFullName()
        name.setText(prefManager.getFullName())
        address.setText(prefManager.getAddress())
        phoneNo.setText(prefManager.getPhoneNo())
        email.setText(prefManager.getEmail())
            firebaseRepository.getUserInfo().addOnSuccessListener { document->
                if (document.data!=null) {
                    Log.d("firebaseRepository", "${document.get("userId")}")
                    prefManager.setCompanyID(document.get("userId").toString());
                    val userInfo = document.toObject(Employee::class.java)
                    prefManager.setFullName(userInfo?.name.toString())
                    prefManager.setAddress(userInfo?.address.toString())
                    prefManager.setEmail(userInfo?.emailId.toString())
                    prefManager.setPhone(userInfo?.phoneNo.toString())
                    //prefManager.setDOJ(userInfo?.time!!.toLong())
                    name.setText(prefManager.getFullName())
                    address.setText(prefManager.getAddress())
                    phoneNo.setText(prefManager.getPhoneNo())
                    email.setText(prefManager.getEmail())
                    tv_name.text = prefManager.getFullName()
                    share_unique_id.visibility=VISIBLE
                    //dateOfJoining.setText(toSimpleDateFormat(prefManager.getDOJ()!!))

                }
            }.addOnFailureListener {
                Toast.makeText(this.requireContext(), "Failed", Toast.LENGTH_SHORT).show()
            }
//        }
        val generator: ColorGenerator = ColorGenerator.MATERIAL
        val color: Int = generator.randomColor
        Handler(Looper.getMainLooper()).postDelayed(object : Runnable {
            override fun run() {
                if (name.text.isNotEmpty()) {
                    drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                        .buildRound(name.text[0].toString().toUpperCase(Locale.ROOT), color)
                    profileImageView.setImageDrawable(drawable)
                } else {
                    drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                        .buildRound("K", color)
                    profileImageView.setImageDrawable(drawable)
                }

            }
        }, 1000)

        share_unique_id.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val shareBody = prefManager.getCompanyId()
            intent.type = "text/plain"
            //intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.share_subject))
            intent.putExtra(Intent.EXTRA_TEXT, shareBody)
            startActivity(Intent.createChooser(intent, null))
        }
        signOut.setOnClickListener {

            AlertDialog.Builder(context).apply {
                setTitle("Are you sure you want to want to sign out?")
                setMessage("You will be returned to the sign in screen")
                setPositiveButton("Yes") { _, _ ->
                    signOut()
                }
                setNegativeButton("Cancel") { _, _ ->

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
                    val df: DocumentReference = fstore.collection("Sales").document(user?.uid!!)
                        .collection("admin").document("Admin Info")
                    df.update("name", name.text.toString(), "address", address.text.toString())
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressBar.visibility = View.GONE
                                updateBtn.visibility = View.GONE
                                edit.visibility = View.VISIBLE
                                name.isEnabled=false
                                address.isEnabled=false
                                Toast.makeText(
                                    this.requireContext(),
                                    "Updated Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this.requireContext(), "Failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
                else{
                    Toast.makeText(
                        this.requireContext(),
                        "Field cannot be empty",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            else{
                Toast.makeText(
                    this.requireContext(),
                    "Please check your Internet connection",
                    Toast.LENGTH_SHORT
                ).show()
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
            }
            catch (e: Exception){
                Toast.makeText(
                    this.requireContext(),
                    "Please check your Internet connection",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

}
