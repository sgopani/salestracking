package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.example.salesadmin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AdminDashboard : Fragment(), View.OnClickListener {

    private lateinit var rootView: View
    private lateinit var addEmployee: CardView
    private lateinit var products:CardView
    private lateinit var user:FirebaseUser
    private lateinit var tvemail:TextView
    private lateinit var parties:CardView
    private lateinit var notification: CardView
    private lateinit var leaveList:CardView
    private lateinit var collectionList:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
     private fun init(){
        addEmployee=rootView.findViewById(R.id.cv_add_employee)
        user= FirebaseAuth.getInstance().currentUser!!
        tvemail=rootView.findViewById(R.id.tv_email_address)
        notification=rootView.findViewById(R.id.cv_send_notification)
        leaveList=rootView.findViewById(R.id.cv_leaves)
        tvemail.text=getString(R.string.Hello,user.email)
         parties=rootView.findViewById(R.id.cv_parties)
         products=rootView.findViewById(R.id.cv_products)
         collectionList=rootView.findViewById(R.id.cv_collections)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        //val transitionListInflater= TransitionInflater.from(requireContext())
        rootView=inflater.inflate(R.layout.admin_dashboard, container, false)
        //exitTransition=transitionListInflater.inflateTransition(R.transition.fade_in)
        init()
        addEmployee.setOnClickListener(this)
        parties.setOnClickListener (this)
        products.setOnClickListener(this)
        notification.setOnClickListener(this)
        leaveList.setOnClickListener(this)
        collectionList.setOnClickListener(this)
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
            R.id.cv_parties->{
                val action=AdminDashboardDirections.actionAdminDashboardToPartiesList()
                findNavController().navigate(action)
            }
            R.id.cv_send_notification->{
                val action=AdminDashboardDirections.actionAdminDashboardToSendNotification()
                findNavController().navigate(action)
            }
            R.id.cv_leaves->{
                val action=AdminDashboardDirections.actionAdminDashboardToLeavesList()
                findNavController().navigate(action)
            }
            R.id.cv_collections ->{
                val action=AdminDashboardDirections.actionAdminDashboardToCollectionList()
                findNavController().navigate(action)
            }

        }
    }

}