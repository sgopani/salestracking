package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.R
import com.example.salesadmin.model.Attendance
import com.example.salesadmin.model.Employee
import com.example.salesadmin.repository.FireStoreViewModel
import java.util.ArrayList

class EmployeeAttendanceList : Fragment() {
    private lateinit var rootView: View
    private lateinit var adapter: AttendanceAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private var attendanceList: MutableList<Attendance> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
    }

    fun init(){
        recyclerView = rootView.findViewById(R.id.rv_attendance_list)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.employee_attendance_list, container, false)
        init()
        adapter = AttendanceAdapter(attendanceList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        //viewModel = FireStoreViewModel()

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.attendanceList.observe(viewLifecycleOwner, Observer {attendance->
            //Toast.makeText(this.requireContext(),"$attendance", Toast.LENGTH_SHORT).show()
            if(attendance!=null){
                attendanceList=attendance
                adapter.attendanceList=attendanceList
                adapter.notifyDataSetChanged()
                viewModel.eventNavigateToAttendanceCompleted()
            }
        })
    }
}