package com.example.salesadmin.leave

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.LeaveItemClickListener
import com.example.salesadmin.R
import com.example.salesadmin.SalesApiStatus
import com.example.salesadmin.isInternetOn
import com.example.salesadmin.model.Leave
import com.example.salesadmin.model.Products
import com.example.salesadmin.repository.FireStoreViewModel
import java.util.*


class LeavesList : Fragment() {
    private lateinit var rootView: View
    private var leaveList: List<Leave> = ArrayList()
    private var searchList: MutableList<Leave> = ArrayList()
    private lateinit var searchEditText: EditText
    private lateinit var adapter: LeaveListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var noLeaves: TextView
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_leaves)
        progressBar = rootView.findViewById(R.id.progress_bar)
        noLeaves=rootView.findViewById(R.id.no_leaves)
        searchEditText=rootView.findViewById(R.id.searchEditText)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

       rootView= inflater.inflate(R.layout.leaves_list, container, false)
        init()
        adapter= LeaveListAdapter(leaveList,getLeaveItemClickListener())
        recyclerView.adapter=adapter
        progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        viewModel = FireStoreViewModel()
        viewModel.getAllLeaves()
        viewModel.leaveList.observe(this.requireActivity(), Observer {leaves->
            leaveList=leaves
            adapter.LeaveList=leaveList
            adapter.notifyDataSetChanged()
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        viewModel.selectedLeave.observe(this.viewLifecycleOwner, Observer { leaves ->
            if (leaves != null) {
                val action= LeavesListDirections.actionLeavesListToLeaveInfo(leaves)
                findNavController().navigate(action)
                //val navController=this.findNavController()
                //navController.navigate(NewsListDirections.actionNewsList2ToNewsInfoFragment2(article))
                //NewsListDirections.actionNewsList2ToNewsInfoFragment2(article)
                viewModel.eventNavigateToLeaveDetailCompleted()
            }
        })
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0.toString())

            }
        })
        return rootView
    }

    private fun filterList(filterItem:String){
        searchList.clear()
        for (item in leaveList) {
            if (item.name.toLowerCase(Locale.ROOT).contains(filterItem.toLowerCase(Locale.ROOT)))
            {
                searchList.add(item)
                adapter.updateList(searchList)
                //noProduct.visibility=View.INVISIBLE
            }
            else {
                if(searchEditText.text.isEmpty()){
                    if(searchList.isEmpty()){
                        //noProduct.visibility=View.GONE
                        adapter.updateList(leaveList)
                        //loadData()
                    }
                }
            }
        }


        adapter.notifyDataSetChanged()
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
                noLeaves.visibility=View.INVISIBLE
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY->{
                noLeaves.visibility=View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
    private fun getLeaveItemClickListener(): LeaveItemClickListener {
        return object : LeaveItemClickListener {

            override fun onLeaveItemClick(leave: Leave) {
                Log.d("onLeaveItemClick","$leave")
                viewModel.eventNavigateToLeaveDetail(leave)
            }
        }
    }

}