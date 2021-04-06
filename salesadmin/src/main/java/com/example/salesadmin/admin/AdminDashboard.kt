package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.fragment.findNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salesadmin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.*

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
    private lateinit var adminImage:ImageView
    private lateinit var order:CardView
    private lateinit var drawable: TextDrawable
    private lateinit var employeeTracking:CardView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
     private fun init(){
         addEmployee=rootView.findViewById(R.id.cv_add_employee)
         user= FirebaseAuth.getInstance().currentUser!!
         tvemail=rootView.findViewById(R.id.tv_email_address)
         notification=rootView.findViewById(R.id.cv_send_notification)
         leaveList=rootView.findViewById(R.id.cv_leaves)
         adminImage=rootView.findViewById(R.id.admin_dashboard_iv)
         order=rootView.findViewById(R.id.cv_order)
         val generator: ColorGenerator = ColorGenerator.MATERIAL
         val color: Int = generator.randomColor
         drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
             .buildRound(user.email!![0].toString().toUpperCase(Locale.ROOT), color)
         adminImage.setImageDrawable(drawable)
         tvemail.text=getString(R.string.Hello,user.email)
         parties=rootView.findViewById(R.id.cv_parties)
         products=rootView.findViewById(R.id.cv_products)
         collectionList=rootView.findViewById(R.id.cv_collections)
         employeeTracking=rootView.findViewById(R.id.cv_employee_tracking)

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
        order.setOnClickListener(this)
        employeeTracking.setOnClickListener(this)
        adminImage.setOnClickListener(this)
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
            R.id.cv_order ->{
                val action=AdminDashboardDirections.actionAdminDashboardToOrderList()
                findNavController().navigate(action)
            }
            R.id.cv_employee_tracking->{
                val action=AdminDashboardDirections.actionAdminDashboardToEmployeeTracking()
                findNavController().navigate(action)
            }
            R.id.admin_dashboard_iv ->{
                val action=AdminDashboardDirections.actionAdminDashboardToProfile()
                findNavController().navigate(action)
            }

        }
    }

}