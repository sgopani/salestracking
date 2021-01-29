package com.example.salesadmin.leave

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.salesadmin.R
import com.example.salesadmin.repository.FireStoreViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LeaveInfo : Fragment() {
    private lateinit var rootView: View
    private lateinit var leaveType:TextView
    private lateinit var fromDate:TextView
    private lateinit var toDate:TextView
    private lateinit var reason:TextView
    private lateinit var status:TextView
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var accept:ImageView
    private lateinit var reject:ImageView
    private lateinit var editButton:FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init(){
        leaveType=rootView.findViewById(R.id.tv_leave_type_info)
        fromDate=rootView.findViewById(R.id.tv_fromdate_info)
        toDate=rootView.findViewById(R.id.tv_todate_info)
        reason=rootView.findViewById(R.id.tv_reason_info)
        status=rootView.findViewById(R.id.tv_status_info)
        accept=rootView.findViewById(R.id.iv_accept)
        reject=rootView.findViewById(R.id.iv_reject)
        viewModel= FireStoreViewModel()
        editButton=rootView.findViewById(R.id.btn_edit)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         rootView=inflater.inflate(R.layout.leave_info, container, false)
        init()
        val leaveDetails =LeaveInfoArgs.fromBundle(requireArguments()).leaves
        val fstore = FirebaseFirestore.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        leaveType.text = leaveDetails.leaveType
        fromDate.text=leaveDetails.fromdate
        toDate.text=leaveDetails.toDate
        reason.text=leaveDetails.reason
        status.text=leaveDetails.status
        accept.setOnClickListener {
            val productList = fstore.collection("Sales")
                .document(user!!.uid).collection("Leaves").document("${leaveDetails.time}")
            productList.update("status","Accepted").addOnCompleteListener {
                Toast.makeText(context,"Leave request accepted",Toast.LENGTH_LONG).show()
                accept.visibility=View.GONE
                reject.visibility=View.GONE
                status.setText(R.string.accepted)
                editButton.visibility=View.VISIBLE
                status.setTextColor(resources.getColor(R.color.green))
            }.addOnFailureListener {
                Log.d("Accepted","Failure")

            }
        }
        editButton.setOnClickListener {
            accept.visibility=View.VISIBLE
            reject.visibility=View.VISIBLE
            editButton.visibility=View.INVISIBLE
        }
        reject.setOnClickListener {
            val productList = fstore.collection("Sales")
                .document(user!!.uid).collection("Leaves").document("${leaveDetails.time}")
            productList.update("status","Rejected").addOnCompleteListener {
                Toast.makeText(context,"Leave request rejected",Toast.LENGTH_LONG).show()
                accept.visibility=View.GONE
                reject.visibility=View.GONE
                editButton.visibility=View.VISIBLE
                status.setText(R.string.rejected)
                status.setTextColor(resources.getColor(R.color.colorRed))
            }.addOnFailureListener {
                Log.d("Rejected","Failure")
            }
        }

        if(status.text == "Accepted" || status.text == "Rejected"){
            accept.visibility=View.GONE
            reject.visibility=View.GONE
            editButton.visibility=View.VISIBLE
        }
        if(status.text=="Rejected"){
            status.setTextColor(resources.getColor(R.color.colorRed))
        }
        if(status.text=="Accepted"){
            status.setTextColor(resources.getColor(R.color.green))
        }

        //Toast.makeText(context,"${leaveDetails.time}",Toast.LENGTH_LONG).show()
        viewModel.selectedLeave.observe(viewLifecycleOwner, Observer {leave->
            //viewModel.getSetImage(newsImage)
            leaveType.text = leave.leaveType
            fromDate.text=leave.fromdate
            toDate.text=leave.toDate
            reason.text=leave.reason
            status.text=leave.status
        })
        return rootView
    }
}