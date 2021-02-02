package com.example.salestracking.collection

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.salestracking.PrefManager
import com.example.salestracking.R
import com.example.salestracking.USER
import com.example.salestracking.databse.model.Collections
import com.example.salestracking.databse.model.Leave
import com.example.salestracking.databse.model.Party
import com.example.salestracking.leave.ApplyLeaveDirections
import com.example.salestracking.repository.FireStoreViewModel
import com.example.salestracking.toSimpleDateFormat
import com.google.firebase.auth.FirebaseAuth
import java.time.temporal.TemporalAmount

class AddCollection : Fragment(), View.OnClickListener {
    private lateinit var date:EditText
    private lateinit var rootView: View
    private lateinit var spinner: Spinner
    private lateinit var addParty:EditText
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var collectionTypeDb:String
    private lateinit var submitBtn:Button
    private lateinit var amount:EditText
    private lateinit var note: EditText
    private var notNull=false
    private  var partyDetails: Party? = null
    private lateinit var prefManager: PrefManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun init(){
        date=rootView.findViewById(R.id.et_date)
        date.setText(toSimpleDateFormat(System.currentTimeMillis()))
        spinner=rootView.findViewById(R.id.collection_spinner)
        addParty=rootView.findViewById(R.id.add_party)
        submitBtn=rootView.findViewById(R.id.btn_collection_submit)
        amount=rootView.findViewById(R.id.et_amount)
        note=rootView.findViewById(R.id.et_note)
        viewModel= FireStoreViewModel()
        prefManager = PrefManager(this.requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.add_collection, container, false)
        init()
        submitBtn.setOnClickListener(this)
        addParty.setOnClickListener (this)
        partyDetails =AddCollectionArgs.fromBundle(requireArguments()).party
        if(partyDetails!=null){
            //Toast.makeText(context, partyDetails?.address,Toast.LENGTH_LONG).show()
            addParty.setText(partyDetails?.name)
        }
        val paymentType = resources.getStringArray(R.array.payment_type)
        val adapter = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item, paymentType)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View?, position: Int, id: Long) {
                collectionTypeDb=paymentType[position]
                //Toast.makeText(context, paymentType[position],Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        return rootView
    }
    private fun checkField(textField: EditText){
        notNull=false
        when {
            textField.text.toString().isEmpty() -> {
                textField.error = "Field cannot be empty"
                notNull = false
            }
            collectionTypeDb.isEmpty() -> {
                notNull=false
            }
            else -> {
                textField.error=null
                notNull=true
            }
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.add_party ->{
                val action= AddCollectionDirections.actionAddCollectionToPartyList()
                findNavController().navigate(action)
            }
            R.id.btn_collection_submit->{
                checkField(addParty)
                checkField(amount)
                checkField(date)
                checkField(note)
                if(notNull){
                    if(addParty.text.isNotEmpty()){
                        addCollection()
                    }
                    else{
                        Toast.makeText(this.requireContext(),"Please select party",Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    private fun addCollection() {
        try {
            val user=FirebaseAuth.getInstance().currentUser
            val collections= Collections(addParty.text.toString(),amount.text.toString().toLong(),date.text.toString(),
            note.text.toString(),collectionTypeDb,prefManager.getFullName().toString(), user?.uid!!,System.currentTimeMillis())
            viewModel.addCollectionsFirebase(collections)
            //progressBar.visibility= View.GONE
            amount.text.clear()
            note.text.clear()
            Toast.makeText(this.requireContext(),"Collection added Successfully",Toast.LENGTH_LONG).show()
            val action= AddCollectionDirections.actionAddCollectionToCollectionList()
            findNavController().navigate(action)
        }
        catch (e:Exception){
         //progressBar.visibility= View.GONE
            Toast.makeText(this.requireContext(),"Something went wrong",Toast.LENGTH_LONG).show()
            Log.i("Error","${e.message}")
        }
    }

}