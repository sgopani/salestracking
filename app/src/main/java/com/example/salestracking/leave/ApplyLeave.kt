package com.example.salestracking.leave

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.room.Room
import com.example.salestracking.R
import com.example.salestracking.toSimpleDateFormat
import java.security.cert.CertPathValidatorException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.MONTH
import javax.xml.datatype.DatatypeConstants.MONTHS

class ApplyLeave : Fragment(), View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var fromDate:EditText
    private lateinit var toDate:EditText
    private lateinit var spinner:Spinner
    private lateinit var reason: EditText
    private lateinit var leaveTypeDb:String
    private lateinit var submitBtn:Button
    private  var startDate:Long=0L
    private  var valid:Boolean=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init(){
        fromDate=rootView.findViewById(R.id.et_from_date)
        toDate=rootView.findViewById(R.id.et_to_date)
        reason=rootView.findViewById(R.id.et_reason)
        spinner=rootView.findViewById(R.id.leave_spinner)
        submitBtn=rootView.findViewById(R.id.btn_submit)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.apply_leave, container, false)
        init()
        fromDate.setOnClickListener(this)
        toDate.setOnClickListener(this)
        submitBtn.setOnClickListener(this)
        val leaveType = resources.getStringArray(R.array.leave_type)
        val adapter = ArrayAdapter(this.requireContext(),
            android.R.layout.simple_spinner_dropdown_item, leaveType)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                leaveTypeDb=getString(R.string.selected_item) + " " +leaveType[position]
//                            "" + leaveType[position]
//                Toast.makeText(context,
//                    getString(R.string.selected_item) + " " +
//                            "" + leaveType[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        return rootView
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.et_from_date->{
                Toast.makeText(this.context,"Clicked",Toast.LENGTH_LONG)
                val calendar = Calendar.getInstance()
                val year = calendar.get(Calendar.YEAR)
                val month = calendar.get(Calendar.MONTH)
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val dpd = DatePickerDialog(this.requireContext(), { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(year,monthOfYear,dayOfMonth)
                     startDate=calendar.timeInMillis
                    //val Month=monthOfYear+1
                    // Display Selected date in textbox

                    fromDate.setText(toSimpleDateFormat(calendar.timeInMillis))
                    valid = fromDate.text.isNotEmpty()
                }, year, month, day)
                dpd.datePicker.minDate=calendar.timeInMillis

                dpd.show()
            }
            R.id.et_to_date->{
                if(valid){
                    val calendar = Calendar.getInstance()
                    val year = calendar.get(Calendar.YEAR)
                    val month = calendar.get(Calendar.MONTH)
                    val day = calendar.get(Calendar.DAY_OF_MONTH)
                    val dpd = DatePickerDialog(this.requireContext(), { view, year, monthOfYear, dayOfMonth ->
                        calendar.set(year,monthOfYear,dayOfMonth)
                        //startDate=calendar.timeInMillis
                        //val Month=monthOfYear+1
                        // Display Selected date in textbox
                        toDate.setText(toSimpleDateFormat(calendar.timeInMillis))
                    }, year, month, day)
                    dpd.datePicker.minDate=startDate
                    dpd.show()
                }
                else{
                  Toast.makeText(this.context,"Please select start date first",Toast.LENGTH_LONG).show()
                }

            }
            R.id.btn_submit->{
                var notNull=false
                fun checkField(textField: EditText){
                    when {
                        textField.text.toString().isEmpty() -> {
                            textField.error = "Field cannot be empty"
                            notNull = false
                        }
                        leaveTypeDb.isEmpty() -> {
                            notNull=false
                        }
                        else -> {
                            textField.error=null
                            notNull=true
                        }
                    }
                }
                checkField(fromDate)
                checkField(toDate)
                checkField(reason)
                if(notNull){
                    Toast.makeText(this.context,
                        fromDate.text.toString()+toDate.text.toString()+leaveTypeDb+reason.text.toString(),Toast.LENGTH_LONG).show()
                    Log.d("data",
                        fromDate.text.toString()+toDate.text.toString()+leaveTypeDb+reason.text.toString()
                    )
                }
            }
        }
    }




}