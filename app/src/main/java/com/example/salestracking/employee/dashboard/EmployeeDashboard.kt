package com.example.salestracking.employee.dashboard

import android.Manifest
import android.app.AlertDialog

import android.os.Build
import android.os.Bundle

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salestracking.*
import com.example.salestracking.databse.model.Attendance
import com.example.salestracking.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
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
    private  var checkInDate:String=""
    private lateinit var prefManager:PrefManager
    private lateinit var checkInOut:CardView
    private lateinit var drawable: TextDrawable
    private lateinit var user: FirebaseUser
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var employeeImage:ImageView
    private lateinit var tvemail: TextView
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
         tvemail=rootView.findViewById(R.id.tv_employee_email)
         viewModel= FireStoreViewModel()
         user= FirebaseAuth.getInstance().currentUser!!
         employeeImage=rootView.findViewById(R.id.iv_dashboard_image)
         val generator: ColorGenerator = ColorGenerator.MATERIAL
         val color: Int = generator.randomColor
         drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                .buildRound(user.email!![0].toString().toUpperCase(Locale.ROOT), color)
         employeeImage.setImageDrawable(drawable)
        tvemail.text=getString(R.string.Hello,user.email)
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
//                val action = EmployeeDashboardDirections.actionEmployeeDashboardToMarkAttendance()
//                findNavController().navigate(action)
                markAttendance()
            }

            R.id.cv_employee_location -> {
                val action = EmployeeDashboardDirections.actionEmployeeDashboardToTracking()
                findNavController().navigate(action)
            }
        }
    }


    private fun markAttendance() {
        if(!isInternetOn(this.requireActivity())) {
            Toast.makeText(this.requireContext(),"Please check your Internet connection",Toast.LENGTH_SHORT).show()
            return
        }
        val valid=prefManager.getIsCheckedIn()
        val fstore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if(isInternetOn(this.requireActivity())) {
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
            .collection("employee")
            .document("${user?.email}").collection("Attendance")
            .document(toSimpleDateFormat(System.currentTimeMillis()))
        df.get().addOnSuccessListener { document ->
            if (document.exists()) {
                prefManager.setIsCheckedIn(false)
                AlertDialog.Builder(context).apply {
                    setTitle("Attendance Marked")
                    setMessage("You have already marked attendance for the day")
                    setPositiveButton("OK") { _, _ ->
                    }
                }.create().show()

            }
            else{
                    prefManager.setIsCheckedIn(true)
                    if (valid) {
                        AlertDialog.Builder(context).apply {
                            setTitle("Are you sure you want to mark your attendance?")
                            setMessage("You can mark only once in a day")
                            setPositiveButton("Yes") { _, _ ->
                                val date = toSimpleDateFormat(System.currentTimeMillis())
                                val checkIn = Calendar.getInstance().time.toString()
                                val attendance = Attendance(
                                    user!!.uid,
                                    System.currentTimeMillis(),
                                    date,
                                    checkIn
                                )
                                try {
                                    viewModel.addAttendanceFirebase(attendance)
                                    checkInDate = date
                                }
                                catch (e:Exception){
                                    Toast.makeText(this.context, "Please check your Internet connection", Toast.LENGTH_SHORT).show()
                                }
                            }
                            setNegativeButton("No") { _, _ ->

                            }
                        }.create().show()
                    }
                }


            }
        }
        else{
            Toast.makeText(this.requireContext(), "Please check your Internet connection", Toast.LENGTH_SHORT).show()
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