package com.example.salestracking.orders

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.salestracking.PrefManager
import com.example.salestracking.R
import com.example.salestracking.collection.AddCollectionArgs
import com.example.salestracking.collection.AddCollectionDirections
import com.example.salestracking.databse.model.Party
import com.example.salestracking.databse.model.Products
import com.example.salestracking.parties.PartyList
import com.example.salestracking.parties.PartyListDirections
import com.example.salestracking.products.ProductsList
import com.example.salestracking.products.ProductsListDirections
import com.example.salestracking.repository.FireStoreViewModel
import com.example.salestracking.requestCode
import com.example.salestracking.toSimpleDateFormat
import com.google.gson.Gson

class AddOrders : Fragment(), View.OnClickListener {
    private lateinit var rootView:View
    private lateinit var addParty:EditText
    private lateinit var date:EditText
    private var partyDetails:Party?=null
    private var productDetails:Products?=null
    private lateinit var addProducts: Button
    private lateinit var viewModel: FireStoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun init(){
        addParty=rootView.findViewById(R.id.add_order_party)
        date=rootView.findViewById(R.id.et_order_date)
        date.setText(toSimpleDateFormat(System.currentTimeMillis()))
        addProducts=rootView.findViewById(R.id.btn_add_product_orders)
        viewModel= FireStoreViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(context,"onDestroy()",Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(context,"onPause()",Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        Toast.makeText(context,"onResume()",Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Toast.makeText(context,"onDestroyView()",Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.add_orders, container, false)
        init()
        Toast.makeText(context,"onCreateView",Toast.LENGTH_SHORT).show()
        viewModel.selectedOrderParty.observe(this.viewLifecycleOwner, Observer { partyList->
            //partyDetails=partyList

            addParty.setText(partyList.name)
            //viewModel.selectedOrderParty.value=partyList
            //partyDetails=partyList

        })
        productDetails=AddOrdersArgs.fromBundle(requireArguments()).products
        if(productDetails!=null){
            Toast.makeText(context, "$productDetails", Toast.LENGTH_LONG).show()
            addProducts.text = productDetails!!.productName
        }

        viewModel.selectedOrderProduct.observe(this.viewLifecycleOwner, Observer { partyList->
            //partyDetails=partyList

            addProducts.text = partyList.productName
            //partyDetails=partyList

        })
        partyDetails = AddOrdersArgs.fromBundle(requireArguments()).party
        //Toast.makeText(context,"${partyDetails.name}",Toast.LENGTH_LONG).show()
        if(partyDetails!=null){
            Toast.makeText(context, partyDetails?.address,Toast.LENGTH_LONG).show()
            addParty.setText(partyDetails?.name)

        }
        addParty.setOnClickListener(this)
        addProducts.setOnClickListener(this)

        return rootView

    }


    override fun onClick(view: View?) {
        when(view?.id){
            R.id.add_order_party->{
                requestCode =0
//                val fragment=PartyList()
//                activity?.fragmentManager?.beginTransaction()?.add(fragment,"String")

                val action=AddOrdersDirections.actionAddOrdersToPartyList()
                findNavController().navigate(action)
            }
            R.id.btn_add_product_orders->{
//                val fragment=ProductsList()
//                val fragmentTransaction= activity?.supportFragmentManager?.beginTransaction()
//                fragmentTransaction?.add(R.id.nav_host_fragment_container,fragment)
//                fragmentTransaction?.addToBackStack(null);
//                fragmentTransaction?.commit()
                //childFragmentManager.beginTransaction()?.add(R.id.container, fragment)?.commit()
                val action=AddOrdersDirections.actionAddOrdersToProductsList()
                findNavController().navigate(action)
            }
        }
    }
}