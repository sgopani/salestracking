package com.example.salestracking.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.salestracking.R
import com.example.salestracking.collection.AddCollectionArgs
import com.example.salestracking.collection.AddCollectionDirections
import com.example.salestracking.databse.model.Party
import com.example.salestracking.requestCode
import com.example.salestracking.toSimpleDateFormat

class AddOrders : Fragment(), View.OnClickListener {
    private lateinit var rootView:View
    private lateinit var addParty:EditText
    private lateinit var date:EditText
    private  var partyDetails: Party? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init(){
        addParty=rootView.findViewById(R.id.add_order_party)
        date=rootView.findViewById(R.id.et_order_date)
        date.setText(toSimpleDateFormat(System.currentTimeMillis()))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.add_orders, container, false)
        init()
        partyDetails = AddOrdersArgs.fromBundle(requireArguments()).party
        if(partyDetails!=null){
            //Toast.makeText(context, partyDetails?.address,Toast.LENGTH_LONG).show()
            addParty.setText(partyDetails?.name)
        }
        addParty.setOnClickListener(this)
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.add_order_party->{
                requestCode =0
                val action=AddOrdersDirections.actionAddOrdersToPartyList()
                findNavController().navigate(action)
            }
        }
    }
}