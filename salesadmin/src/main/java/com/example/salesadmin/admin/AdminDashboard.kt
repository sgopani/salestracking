package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import androidx.transition.TransitionInflater
import com.example.salesadmin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AdminDashboard : Fragment(), View.OnClickListener {

    private lateinit var rootView: View
    private lateinit var addEmployee: CardView
    private lateinit var products:CardView
    private lateinit var user:FirebaseUser
    private lateinit var tvemail:TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val transitionListInflater= TransitionInflater.from(requireContext())
        rootView=inflater.inflate(R.layout.admin_dashboard, container, false)
        //exitTransition=transitionListInflater.inflateTransition(R.transition.fade_in)
        addEmployee=rootView.findViewById(R.id.cv_add_employee)
        user= FirebaseAuth.getInstance().currentUser!!
        tvemail=rootView.findViewById(R.id.tv_email_address)
        tvemail.text=getString(R.string.Hello,user.email)
        addEmployee.setOnClickListener(this)
        products=rootView.findViewById(R.id.cv_products)
        products.setOnClickListener(this)
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.cv_add_employee ->{
                val action=AdminDashboardDirections.actionAdminDashboardToEmployeeList()
                findNavController().navigate(action)
            }
            R.id.cv_products->{
                val action=AdminDashboardDirections.actionAdminDashboardToProductsList()
                findNavController().navigate(action)
            }
        }
    }

}