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
//import com.example.salesadmin.CheckNetClass
//import com.example.salesadmin.R
//import com.example.salesadmin.model.Party
//import com.example.salesadmin.repository.FireStoreViewModel
//import com.google.android.gms.common.ConnectionResult
//import com.google.android.gms.common.api.GoogleApiClient
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.net.PlacesClient
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.FirebaseFirestore
//
//
//class AddParties : Fragment(),GoogleApiClient.OnConnectionFailedListener  {
//    private lateinit var rootView: View
//    private lateinit var partyName:EditText
//    private lateinit var partyAddress:EditText
//    private lateinit var phoneNumber:EditText
//    private lateinit var contactName:EditText
//    private lateinit var addPartyBtn:Button
//    //private lateinit var progressBar: ProgressBar
//    private var valid:Boolean=true
//    private lateinit var viewModel: FireStoreViewModel
//    private lateinit var user: FirebaseUser
//    private lateinit var fstore: FirebaseFirestore
//    private lateinit var selectLocation:Button
//    private lateinit var mGoogleApiClient: GoogleApiClient
//    private var PLACE_PICKER_REQUEST = 1;
////    AIzaSyAJ5byQM3At42_58O5RGTHwWXQHQwrM6_o
//
//    private fun init(){
//        partyName=rootView.findViewById(R.id.et_party_name)
//        partyAddress=rootView.findViewById(R.id.et_party_address)
//        phoneNumber=rootView.findViewById(R.id.et_party_phone)
//        contactName=rootView.findViewById(R.id.et_contact_person)
//        addPartyBtn=rootView.findViewById(R.id.btn_add_party)
//        //progressBar=rootView.findViewById(R.id.progress_bar)
//        //selectLocation=rootView.findViewById(R.id.Select_location)
//        fstore= FirebaseFirestore.getInstance()
//        viewModel= FireStoreViewModel()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        val apiKey = getString(R.string.api_key)
////        mGoogleApiClient = GoogleApiClient.Builder(this.requireContext())
////            .addApi(Places.GEO_DATA_API)
////            .addApi(Places.PLACE_DETECTION_API)
////            .enableAutoManage(this.requireActivity(), GoogleApiClient.OnConnectionFailedListener {
////                Toast.makeText(
////                    this.requireContext(),
////                    "GoogleApiClient.OnConnectionFailedListener",
////                    Toast.LENGTH_SHORT
////                ).show()
////            })
////            .build()
//        if (!Places.isInitialized()) {
//            Places.initialize(this.requireContext(), apiKey);
//        }
//        val placesClient: PlacesClient = Places.createClient(this.requireContext())
//        super.onCreate(savedInstanceState)
//    }
//    override fun onPause() {
//        super.onPause()
//        //mGoogleApiClient.stopAutoManage(this.requireActivity())
//        //mGoogleApiClient.disconnect()
//    }
//    override fun onStart() {
//        super.onStart()
//        //mGoogleApiClient.connect()
//    }
//    override fun onConnectionFailed(connectionResult: ConnectionResult) {
////        Snackbar.make(
////            selectLocation,
////            connectionResult.getErrorMessage().toString() + "",
////            Snackbar.LENGTH_LONG
////        ).show()
//    }
//
//    override fun onStop() {
//        //mGoogleApiClient.disconnect()
//        super.onStop()
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        rootView= inflater.inflate(R.layout.add_parties, container, false)
//        init()
//
//        addPartyBtn.setOnClickListener {
//            addParty()
//        }
//        //selectLocation.setOnClickListener {
////            val builder = PlacePicker.IntentBuilder()
////            try {
////                startActivityForResult(builder.build(this.activity), PLACE_PICKER_REQUEST)
////            } catch (e: GooglePlayServicesRepairableException) {
////                e.printStackTrace()
////            } catch (e: GooglePlayServicesNotAvailableException) {
////                e.printStackTrace()
////            }
//        //}
//        return rootView
//    }
////    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
////        if (requestCode == PLACE_PICKER_REQUEST) {
////            if (resultCode == RESULT_OK) {
////                val place: Place = PlacePicker.getPlace(data, this.requireContext())
////                val stBuilder = StringBuilder()
////                val placename = java.lang.String.format("%s", place.getName())
////                val latitude: String = java.lang.String.valueOf(place.getLatLng().latitude)
////                val longitude: String = java.lang.String.valueOf(place.getLatLng().longitude)
////                val address = java.lang.String.format("%s", place.getAddress())
////                stBuilder.append("Name: ")
////                stBuilder.append(placename)
////                stBuilder.append("\n")
////                stBuilder.append("Latitude: ")
////                stBuilder.append(latitude)
////                stBuilder.append("\n")
////                stBuilder.append("Logitude: ")
////                stBuilder.append(longitude)
////                stBuilder.append("\n")
////                stBuilder.append("Address: ")
////                stBuilder.append(address)
////                partyAddress.setText(stBuilder.toString())
////            }
////        }
////    }
//
//    private fun addParty() {
//        checkField(partyName)
//        checkField(partyAddress)
//        checkField(phoneNumber)
//        checkField(contactName)
//        if (CheckNetClass.checknetwork(this.requireContext())) {
//            if (valid) {
//                //progressBar.visibility = View.VISIBLE
//                checkIfPartyExist()
//            } else {
//                Toast.makeText(
//                    this.requireContext(),
//                    "Please enter fields properly",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//        }
//        else{
//
//            Toast.makeText(
//                this.requireContext(),
//                "Sorry,no internet connectivty",
//                Toast.LENGTH_LONG
//            ).show()
//        }
//    }
//
//    private fun checkIfPartyExist() {
//        val id=partyName.text.toString()
//        user= FirebaseAuth.getInstance().currentUser!!
//        val documentReference=fstore.collection("Sales").document(user?.uid!!).collection("Party")
//            .document(partyName.text.toString())
//        documentReference.get().addOnSuccessListener { document->
//            if(document.exists()) {
//                //progressBar.visibility= View.GONE
//                Toast.makeText(
//                    this.requireContext(),
//                    "Party with this name already exist",
//                    Toast.LENGTH_LONG
//                ).show()
//            }
//            else{
//                registerParty()
//            }
//        }
//    }
//
//    private fun registerParty() {
//        try {
//            val time=System.currentTimeMillis()
//            val party= Party(
//                partyName.text.toString(),
//                partyAddress.text.toString(),
//                phoneNumber.text.toString(),
//                contactName.text.toString(),
//                partyName.text.toString(),
//                time
//            )
//            viewModel.addParty(party)
//            Toast.makeText(this.requireContext(), "Party added successfully", Toast.LENGTH_LONG).show()
//            val action=AddPartiesDirections.actionAddPartiesToPartiesList()
//            findNavController().navigate(action)
//            //progressBar.visibility=View.GONE
//        }
//        catch (e: Exception){
//            //progressBar.visibility=View.GONE
//            Toast.makeText(this.requireContext(), "Something went wrong", Toast.LENGTH_LONG).show()
//            Log.d("Unable to add", "Unable to add")
//        }
//    }
//
//    private fun checkField(textField: EditText): Boolean {
//        when {
//            textField.text.toString().isEmpty() -> {
//                textField.error = "Field cannot be empty"
//                valid = false
//            }
//            partyAddress.text.toString().length < 10 -> {
//                partyAddress.error = "Enter proper address"
//                valid=false
//            }
//            Patterns.PHONE.matcher(phoneNumber.text.toString()).matches() -> {
//                if (phoneNumber.text.length == 10) {
//                    valid = true
//                } else {
//                    phoneNumber.error = "Please enter correct mobile number"
//                    valid = false
//                }
//            }
//            else->{
//                valid=true
//            }
//        }
//        return valid
//    }
//}