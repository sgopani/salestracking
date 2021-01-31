package com.example.salestracking.collection

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.example.salestracking.R
import com.example.salestracking.toSimpleDateFormat

class AddCollection : Fragment() {
    private lateinit var date:EditText
    private lateinit var rootView: View
    private lateinit var spinner: Spinner
    private lateinit var addParty:EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.add_collection, container, false)
        date=rootView.findViewById(R.id.et_date)
        date.setText(toSimpleDateFormat(System.currentTimeMillis()))
        spinner=rootView.findViewById(R.id.collection_spinner)
        addParty=rootView.findViewById(R.id.add_party)
        addParty.setOnClickListener {
            val action= AddCollectionDirections.actionAddCollectionToPartyList()
            findNavController().navigate(action)
        }
        val partyDetails =AddCollectionArgs.fromBundle(requireArguments()).party

        if(partyDetails!=null){
            Toast.makeText(context, partyDetails?.address,Toast.LENGTH_LONG).show()
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
                Toast.makeText(context, paymentType[position],Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        return rootView
    }
}