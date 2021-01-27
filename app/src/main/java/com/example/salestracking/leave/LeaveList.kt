package com.example.salestracking.leave

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.R
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.Leave
import com.example.salestracking.isInternetOn
import com.example.salestracking.models.Notification
import com.example.salestracking.notification.NotificationAdapter
import com.example.salestracking.repository.FireStoreViewModel
import java.util.ArrayList

class LeaveList : Fragment() {
    private lateinit var rootView:View
    private lateinit var applyLeaveBtn:Button
    private var leaveList: List<Leave> = ArrayList()
    private lateinit var adapter: LeaveListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }
    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_leave_list)
        progressBar = rootView.findViewById(R.id.progress_bar)
        //noNotification=rootView.findViewById(R.id.no_notification)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.leave_list, container, false)
        init()
        adapter= LeaveListAdapter(leaveList)
        recyclerView.adapter=adapter
        progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        viewModel = FireStoreViewModel()
        viewModel.getAllLeaveList()

        viewModel.leaveList.observe(this.requireActivity(), Observer {leaves->
            leaveList=leaves
            adapter.LeaveList=leaveList
            adapter.notifyDataSetChanged()
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        applyLeaveBtn=rootView.findViewById(R.id.apply_leave_btn)
        applyLeaveBtn.setOnClickListener{
            val action=LeaveListDirections.actionLeaveListToApplyLeave()
            findNavController().navigate(action)
        }
        return rootView
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
                    Toast.makeText(this.context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = View.GONE

            }
            SalesApiStatus.DONE -> {
                //noNotification.visibility=View.INVISIBLE
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY->{
                //noNotification.visibility=View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

}