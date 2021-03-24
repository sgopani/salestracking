package com.example.salestracking.attendance


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.salestracking.COMPANYUID
import com.example.salestracking.PrefManager
import com.example.salestracking.R
import com.example.salestracking.databse.model.Attendance
import com.example.salestracking.repository.FireStoreViewModel
import com.example.salestracking.toSimpleDateFormat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class MarkAttendance :Fragment()  {
    private lateinit var markAttendance:Button
    private lateinit var rootView: View
    private lateinit var prefManager: PrefManager
    private lateinit var viewModel: FireStoreViewModel
    private  var checkInDate:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init(){
        markAttendance=rootView.findViewById(R.id.btn_mark_attendance)
        prefManager=PrefManager(this.requireContext())
        viewModel= FireStoreViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.markattendance, container, false)
        init()
        markAttendance.setOnClickListener{
            markAttendance()
        }
        return rootView
    }

    private fun markAttendance() {
        val valid=prefManager.getIsCheckedIn()
        val fstore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser

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
            }else{
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
                            viewModel.addAttendanceFirebase(attendance)
                            checkInDate = date
                            //prefManager.setIsCheckedIn(true)
                            //checkOutBtn.visibility = View.VISIBLE
                            //checkInBtn.visibility = View.INVISIBLE
                        }
                        setNegativeButton("No") { _, _ ->

                        }
                    }.create().show()
                }

            }

        }

    }

}