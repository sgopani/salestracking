package com.example.salesadmin.admin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.*
import com.example.salesadmin.model.Employee
import com.example.salesadmin.repository.FireStoreViewModel
import java.util.*

class EmployeeList : Fragment() {
    private lateinit var rootView: View
    private lateinit var adapter: EmployeeListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noEmployee: TextView
    private var employeeList: MutableList<Employee> = ArrayList()
    private var searchList: MutableList<Employee> = ArrayList()
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
    }
    fun init() {
        recyclerView = rootView.findViewById(R.id.rv_employeeList)
        noEmployee=rootView.findViewById(R.id.no_employee)
        progressBar = rootView.findViewById(R.id.progress_bar)
        searchEditText=rootView.findViewById(R.id.searchEditText)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.employee_list, container, false)
        init()
        progressBar.visibility = View.VISIBLE
        adapter = EmployeeListAdapter(employeeList,getEmployeeItemClickListener())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        //viewModel = FireStoreViewModel()
        viewModel.getAllEmployee()

        viewModel.employeeList.observe(this.requireActivity(), Observer { employee ->
            noEmployee.visibility = View.INVISIBLE
            employeeList = employee
            adapter.employeeList = employeeList
            adapter.notifyDataSetChanged()
        })


        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
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
        return  rootView

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.attendanceList.observe(viewLifecycleOwner, Observer {attendance->
            if(attendance!=null){
                //Toast.makeText(this.requireContext(),"$attendance", Toast.LENGTH_SHORT).show()
                val action=EmployeeListDirections.actionEmployeeListToEmployeeAttendanceList()
                findNavController().navigate(action)
            }
        })
    }

    private fun filterList(filterItem:String){
        searchList.clear()
        for (item in employeeList) {
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
                        adapter.updateList(employeeList)
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
    private fun getEmployeeItemClickListener(): EmployeeAttendanceItemClickListener{
        return object :  EmployeeAttendanceItemClickListener {

            override fun onEmployeeAttendanceClick(email: String) {
                viewModel.getEmployeeAttendance(email)
            }
        }
    }

}