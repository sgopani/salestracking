package com.example.salestracking.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.*
import com.example.salestracking.collection.CollectionListDirections
import com.example.salestracking.databse.model.Collections
import com.example.salestracking.databse.model.Leave
import com.example.salestracking.databse.model.Order
import com.example.salestracking.leave.LeaveListAdapter
import com.example.salestracking.repository.FireStoreViewModel
import java.util.ArrayList

class OrderList : Fragment() {
    private lateinit var rootView: View
    private var orderList: MutableList<Order> = ArrayList()
    private lateinit var adapter: OrderListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var takeOrderBtn:Button
    private lateinit var noOrder:TextView
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
    }
    private fun init(){
        //viewModel= FireStoreViewModel()
        recyclerView=rootView.findViewById(R.id.rv_order_list)
        takeOrderBtn=rootView.findViewById(R.id.btn_take_order)
        progressBar=rootView.findViewById(R.id.progress_bar)
        noOrder=rootView.findViewById(R.id.no_orders)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.order_list, container, false)
        init()
        configureLeaveList()
        viewModel.getAllOrderList()
        viewModel.orderList.observe(this.requireActivity(), Observer { orders ->
            //Log.d("orderList()2","$orders")
            //Toast.makeText(context,"$orders",Toast.LENGTH_LONG).show()
            orderList=orders
            adapter.OrderList = orderList
            adapter.notifyDataSetChanged()
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        takeOrderBtn.setOnClickListener {
            requestCode=0
            val action=OrderListDirections.actionOrderListToPartyList()
            findNavController().navigate(action)
        }
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.selectedOrderDetails.observe(viewLifecycleOwner, Observer {order ->
            if (order != null) {
                val action= OrderListDirections.actionOrderListToOrderDetails(order)
                findNavController().navigate(action)
            }
        })
    }

    private fun configureLeaveList(){
        adapter= OrderListAdapter(orderList,getOrderItemClickListener())
        recyclerView.adapter=adapter
        progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        //itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun checkInternet(status: SalesApiStatus) {
        when (status) {
            SalesApiStatus.LOADING -> {
                progressBar.visibility=View.VISIBLE
            }
            SalesApiStatus.ERROR -> {
                if (isInternetOn(this.requireContext())) {
                    Toast.makeText(this.context, "Connected to internet", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.newsList2)
                } else {
                    Toast.makeText(
                        this.context,
                        "Please Check Your Internet Connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.DONE -> {
                noOrder.visibility = View.INVISIBLE
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY -> {
                noOrder.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
    private fun getOrderItemClickListener(): OrderDetailsItemClickListener {
        return object : OrderDetailsItemClickListener {
            override fun onOrderItemClick(order: Order) {
                Log.d("onOrderItemClick","$order")
                viewModel.eventNavigateToOrderDetail(order)
            }
        }
    }

}