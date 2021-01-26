package com.example.salesadmin.admin

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.salesadmin.CheckNetClass
import com.example.salesadmin.R
import com.example.salesadmin.model.Party
import com.example.salesadmin.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.JsonObject
import org.json.JSONException
import org.json.JSONObject


class AddParties : Fragment() {
    private lateinit var rootView: View
    private lateinit var partyName: EditText
    private lateinit var partyAddress: EditText
    private lateinit var phoneNumber: EditText
    private lateinit var contactName: EditText
    private lateinit var addPartyBtn: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var pinCodeEdit: EditText
    private var valid: Boolean = true
    private  var checkPinCode=false
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var user: FirebaseUser
    private lateinit var fstore: FirebaseFirestore
    private lateinit var mRequestQueue: RequestQueue
    private lateinit var district_tv:TextView
    private lateinit var state_tv:TextView
    private lateinit var check:TextView
    private lateinit var verified_image:ImageView
    private lateinit var edit_tv:TextView
    private fun init() {
        partyName = rootView.findViewById(R.id.et_party_name)
        partyAddress = rootView.findViewById(R.id.et_party_address)
        phoneNumber = rootView.findViewById(R.id.et_party_phone)
        contactName = rootView.findViewById(R.id.et_contact_person)
        addPartyBtn = rootView.findViewById(R.id.btn_add_party)
        progressBar = rootView.findViewById(R.id.progress_bar)
        pinCodeEdit = rootView.findViewById(R.id.et_pincode)
        fstore = FirebaseFirestore.getInstance()
        check=rootView.findViewById(R.id.check_tv)
        viewModel = FireStoreViewModel()
        district_tv=rootView.findViewById(R.id.tv_district)
        state_tv=rootView.findViewById(R.id.tv_state)
        verified_image=rootView.findViewById(R.id.verified_iv)
        edit_tv=rootView.findViewById(R.id.edit_tv)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        mRequestQueue = Volley.newRequestQueue(this.requireActivity());
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.add_parties, container, false)
        init()
        check.setOnClickListener {
            getDataFromPinCode(pinCodeEdit.text.toString())
        }
        edit_tv.setOnClickListener{
            checkPinCode=false
            pinCodeEdit.isEnabled=true
            check.visibility=View.VISIBLE
            verified_image.visibility=View.GONE
            edit_tv.visibility=View.INVISIBLE
            district_tv.text=""
            state_tv.text=""
        }
        addPartyBtn.setOnClickListener {
            if(checkPinCode){
                addParty()
            }
            else{
                pinCodeEdit.error="Please Check Pin code"
            }

        }
        return rootView
    }

    private fun addParty() {
        if (CheckNetClass.checknetwork(this.requireContext())) {
            checkField(partyName)
            checkField(partyAddress)
            checkField(phoneNumber)
            checkField(contactName)
            checkField(pinCodeEdit)
//            if(checkPinCode){
            if (valid) {
                if(checkPinCode){
                    progressBar.visibility = View.VISIBLE
                    checkIfPartyExist()
                }
                else{
                    pinCodeEdit.error="Pin code is not valid"
                    progressBar.visibility = View.GONE
                }

            } else {
                Toast.makeText(
                        this.requireContext(),
                        "Please enter fields properly",
                        Toast.LENGTH_SHORT
                ).show()
            }
//            else{
//                valid=false
//                pinCodeEdit.error="Pin code is not valid"
//            }
        } else {

            Toast.makeText(
                    this.requireContext(),
                    "Sorry,no internet connectivty",
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkIfPartyExist() {
        val id = partyName.text.toString()
        user = FirebaseAuth.getInstance().currentUser!!
        val documentReference = fstore.collection("Sales")
                .document(user?.uid!!).collection("Party")
                .document(partyName.text.toString())
        documentReference.get().addOnSuccessListener { document ->
            if (document.exists()) {
                progressBar.visibility = View.GONE
                Toast.makeText(
                        this.requireContext(),
                        "Party with this name already exist",
                        Toast.LENGTH_LONG
                ).show()
            } else {
                registerParty()
            }
        }
    }

    private fun registerParty() {
        try {
            val time = System.currentTimeMillis()
            //Log.d("Pin code", "${pinCodeEdit.text.toString()}")
            val party = Party(
                    partyName.text.toString(),
                    partyAddress.text.toString()+","+ district_tv.text.toString()+","+ state_tv.text.toString(),
                    phoneNumber.text.toString(),
                    contactName.text.toString(),
                    partyName.text.toString(),
                    time
            )
            viewModel.addParty(party)
            Toast.makeText(this.requireContext(), "Party added successfully", Toast.LENGTH_LONG).show()
            val action = AddPartiesDirections.actionAddPartiesToPartiesList()
            findNavController().navigate(action)
            progressBar.visibility = View.GONE
        } catch (e: Exception) {
            progressBar.visibility = View.GONE
            Toast.makeText(this.requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
            Log.d("Unable to add", "Unable to add")
        }
    }

    private fun checkField(textField: EditText): Boolean {
        when {
            textField.text.toString().isEmpty() -> {
                textField.error = "Field cannot be empty"
                valid = false
            }
            partyAddress.text.toString().length < 10 -> {
                partyAddress.error = "Enter proper address"
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
            pinCodeEdit.text.toString().length !=6 ->{
                pinCodeEdit.error = "Please enter correct pin code"
                valid = false
            }

            else -> {
                valid = true
            }
        }
        return valid
    }

    private fun getDataFromPinCode(pinCode: String) {
        progressBar.visibility=View.VISIBLE
        mRequestQueue.cache.clear()

        val url = "http://www.postalpincode.in/api/pincode/$pinCode"

        // below line is use to initialize our request queue.
        val queue = Volley.newRequestQueue(this.requireActivity())
        val objectRequest= JsonObjectRequest(Request.Method.GET, url,null, { response ->
            try {
                val postOfficeArray = response.getJSONArray("PostOffice")
                if (response.getString("Status") == "Error") {
                    pinCodeEdit.error="Pin code is not valid"
                    Toast.makeText(this.context,"Pin code is not valid.",Toast.LENGTH_LONG).show()
                    checkPinCode=false
                    valid=false
                    progressBar.visibility=View.GONE
                    //pinCodeDetailsTV.setText("Pin code is not valid.")
                } else {
                    val obj = postOfficeArray.getJSONObject(0)
                    val name=obj.getString("Name")
                    val district = obj.getString("District")
                    val state = obj.getString("State")
                    val country = obj.getString("Country")
                    progressBar.visibility=View.GONE
                    checkPinCode=true
                    if(checkPinCode){
                        edit_tv.visibility=View.VISIBLE
                        pinCodeEdit.isEnabled=false
                        pinCodeEdit.error=null
                    }
                    valid=true
                    district_tv.text=district
                    state_tv.text=state
                    check.visibility=View.GONE
                    verified_image.visibility=View.VISIBLE
                }
            } catch (e: JSONException) {
                e.printStackTrace()
                pinCodeEdit.error="Pin code is not valid"
                valid=false
                checkPinCode=false
                progressBar.visibility=View.GONE
            }
        }, { error->
            Log.d("Unable to add", "${error.message}")
            Toast.makeText(this.context, "Pin code is not valid.", Toast.LENGTH_SHORT).show()
            pinCodeEdit.error=("Pin code is not valid")
            checkPinCode=false
            valid=false
            progressBar.visibility=View.GONE
        })
        queue.add(objectRequest)
    }



}