package com.example.salestracking.employee.dashboard

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.SettingNotFoundException
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.salestracking.*
import com.example.salestracking.databse.model.Attendance
import com.example.salestracking.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.util.*


class EmployeeDashboard : Fragment(), View.OnClickListener,EasyPermissions.PermissionCallbacks {
    private lateinit var notes: CardView
    private lateinit var applyLeave:CardView
    private lateinit var collections:CardView
    private lateinit var orders:CardView
    private lateinit var attendence:CardView
    private lateinit var rootView: View
    private lateinit var checkInBtn:Button
    private lateinit var checkOutBtn:Button
    private  var isCheckIn=false

    private lateinit var prefManager:PrefManager
    private lateinit var checkInOut:CardView


    private lateinit var viewModel: FireStoreViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
//    tools:context=".products.ProductList">
     private fun init(){
         notes=rootView.findViewById(R.id.notes)
         applyLeave=rootView.findViewById(R.id.cv_apply_leave)
         collections=rootView.findViewById(R.id.cv_collections)
         orders=rootView.findViewById(R.id.cv_take_orders)
         checkInBtn=rootView.findViewById(R.id.btn_check_in)
         checkOutBtn=rootView.findViewById(R.id.btn_check_out)
         attendence=rootView.findViewById(R.id.cv_attendence)
         checkInOut=rootView.findViewById(R.id.cv_employee_location)
         prefManager=PrefManager(this.requireContext())
         viewModel= FireStoreViewModel()
    }
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        //val user = FirebaseAuth.getInstance().currentUser
        rootView= inflater.inflate(R.layout.fragment_employee, container, false)
        init()
        collections.setOnClickListener(this)
        applyLeave.setOnClickListener(this)
        notes.setOnClickListener(this)
        orders.setOnClickListener(this)
        checkInBtn.setOnClickListener(this)
        checkOutBtn.setOnClickListener(this)
        checkInOut.setOnClickListener(this)
        isCheckIn=prefManager.getIsCheckedIn()
        attendence.setOnClickListener(this)
        if(isCheckIn){
            //checkOutBtn.visibility=View.VISIBLE
            //checkInBtn.visibility=View.GONE
        }
        else{
            //checkInBtn.visibility=View.VISIBLE
            //checkOutBtn.visibility=View.GONE
        }
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.notes -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToNotes2()
                findNavController().navigate(action)
            }
            R.id.cv_apply_leave -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToLeaveList()
                findNavController().navigate(action)
            }
            R.id.cv_collections -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToCollectionList()
                findNavController().navigate(action)
            }
            R.id.cv_take_orders -> {
//                val action = EmployeeDashboardDirections.actionEmployeeDashboardToAddOrders()
//                findNavController().navigate(action)
                requestCode = 0
                val tag = "String"
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToOrderList()
                findNavController().navigate(action)
            }
            R.id.btn_check_in -> {
                //markAttendance()
            }
            R.id.btn_check_out -> {
                //prefManager.setIsCheckedIn(false)
                //checkInBtn.visibility=View.VISIBLE
                //checkOutBtn.visibility=View.GONE
            }
            R.id.cv_attendence -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToMarkAttendance()
                findNavController().navigate(action)
                //markAttendance()
            }

            R.id.cv_employee_location -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToTracking()
                findNavController().navigate(action)
            }
        }
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()
    }
    private fun requestPermissions() {
        if(TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
        else {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onResume() {
        super.onResume()
        requestPermissions()
    }


    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }


    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}