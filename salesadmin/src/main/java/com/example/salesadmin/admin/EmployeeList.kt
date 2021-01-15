package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.R
import com.example.salesadmin.SalesApiStatus
import com.example.salesadmin.isInternetOn
import com.example.salesadmin.model.Employee
import com.example.salesadmin.model.Products
import com.example.salesadmin.repository.FireStoreViewModel
import java.util.ArrayList

class EmployeeList : Fragment() {
    private lateinit var rootView: View
    private lateinit var adapter: EmployeeListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noEmployee: TextView
    private var employeeList: List<Employee> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    fun init() {
        recyclerView = rootView.findViewById(R.id.rv_employeeList)
        noEmployee=rootView.findViewById(R.id.no_employee)
        progressBar = rootView.findViewById(R.id.progress_bar)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.employee_list, container, false)
        init()
        progressBar.visibility = View.VISIBLE
        adapter=EmployeeListAdapter(employeeList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        viewModel = FireStoreViewModel()
        viewModel.getAllEmployee()
        viewModel.employeeList.observe(this.requireActivity(), Observer {employee->
            noEmployee.visibility=View.INVISIBLE
            employeeList=employee
            adapter.employeeList = employeeList
            adapter.notifyDataSetChanged()
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        return  rootView

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
                noEmployee.visibility=View.INVISIBLE
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY->{
                noEmployee.text=getString(R.string.no_employee)
                noEmployee.visibility=View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }

}