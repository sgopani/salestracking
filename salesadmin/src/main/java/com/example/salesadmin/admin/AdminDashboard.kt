package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.salesadmin.R

class AdminDashboard : Fragment(), View.OnClickListener {

    private lateinit var rootView: View
    private lateinit var addEmployee: CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView=inflater.inflate(R.layout.admin_dashboard, container, false)
        addEmployee=rootView.findViewById(R.id.cv_add_employee)
        addEmployee.setOnClickListener(this)
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.cv_add_employee ->{
                val action=AdminDashboardDirections.actionAdminDashboardToAddEmployee()
                findNavController().navigate(action)
            }
        }
    }

}