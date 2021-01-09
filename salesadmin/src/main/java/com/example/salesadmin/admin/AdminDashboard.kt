package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.salesadmin.R

class AdminDashboard : Fragment(), View.OnClickListener {

    private lateinit var rootView: View
    private lateinit var addEmployee: CardView
    private lateinit var products:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val transitionListInflater= TransitionInflater.from(requireContext())
        rootView=inflater.inflate(R.layout.admin_dashboard, container, false)
        //exitTransition=transitionListInflater.inflateTransition(R.transition.fade_in)
        addEmployee=rootView.findViewById(R.id.cv_add_employee)
        addEmployee.setOnClickListener(this)
        products=rootView.findViewById(R.id.cv_products)
        products.setOnClickListener(this)
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.cv_add_employee ->{
                val action=AdminDashboardDirections.actionAdminDashboardToAddEmployee()
                findNavController().navigate(action)
            }
            R.id.cv_products->{
                val action=AdminDashboardDirections.actionAdminDashboardToAddProduct()
                findNavController().navigate(action)
            }
        }
    }

}