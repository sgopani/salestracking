package com.example.salesadmin.orders

import android.os.Bundle
import android.text.util.Linkify
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.R
import com.example.salesadmin.model.CartItem
import com.example.salesadmin.repository.FireStoreViewModel
import com.example.salesadmin.toSimpleDateFormat
import com.example.salesadmin.toTimeFormat
import java.util.ArrayList

class OrderDetails : Fragment() {
    private lateinit var rootView: View
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var adapter: OrderDetailsAdapter
    private lateinit var recyclerView: RecyclerView
    private var orderDetailsList: MutableList<CartItem> = ArrayList()
    private lateinit var  tvPartyName:TextView
    private lateinit var tvPartyAddress:TextView
    private lateinit var tvPartyContact:TextView
    private lateinit var tvOrderDate:TextView
    private lateinit var tvOrderTotal:TextView
    private lateinit var tvOrderPartyEmail:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
        //viewModel= FireStoreViewModel()
    }
    private fun init(){
        //
        recyclerView = rootView.findViewById(R.id.rv_order_details)
        tvPartyName=rootView.findViewById(R.id.tv_party_name_order)
        tvPartyAddress=rootView.findViewById(R.id.tv_party_address_order)
        tvPartyContact=rootView.findViewById(R.id.tv_order_party_contact_no)
        tvOrderDate=rootView.findViewById(R.id.tv_order_details_date)
        tvOrderTotal=rootView.findViewById(R.id.tv_order_detail_total)
        tvOrderPartyEmail=rootView.findViewById(R.id.tv_order_party_contact)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.order_details, container, false)
        init()
        configureLeaveList()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.selectedOrderDetails.observe(viewLifecycleOwner, Observer {order->
            if(order!=null){
                //Toast.makeText(context,"${order.productlist}",Toast.LENGTH_LONG).show()
                adapter.CartList=order.productlist
                tvPartyName.text= order.party!!.name
                tvPartyAddress.text= order.party!!.address
                tvPartyContact.text= order.party!!.phoneNo
                val date= toSimpleDateFormat(order.time.toLong())+" at "+toTimeFormat(order.time.toLong())
                tvOrderDate.text=date
                tvOrderTotal.text=order.total
                tvOrderPartyEmail.text= order.party!!.contactName
                Linkify.addLinks(tvPartyContact, Linkify.PHONE_NUMBERS)
                viewModel.eventNavigateToOrderDetailCompleted()
            }
            //viewModel.eventNavigateToOrderDetailCompleted()
        })
    }
    private fun configureLeaveList(){
        adapter= OrderDetailsAdapter(orderDetailsList)
        recyclerView.adapter=adapter
        //progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        //itemTouchHelper.attachToRecyclerView(recyclerView)
    }
}