package com.example.salesadmin.admin

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.R
import com.example.salesadmin.model.Employee

class EmployeeListAdapter(var employeeList: List<Employee>): RecyclerView.Adapter<EmployeeListAdapter.EmployeeItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EmployeeItem{
        return EmployeeItem.createViewHolder(parent)
    }
    private fun getItem(position: Int): Employee {
        return employeeList[position]
    }

    override fun getItemCount(): Int {
        return employeeList.size
    }
    override fun onBindViewHolder(holder: EmployeeItem, position: Int) {
        val employee:Employee=getItem(position)
        holder.bind(employee)
    }
    class EmployeeItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvEmployeeName=itemView.findViewById<TextView>(R.id.tv_notification_message)
        val tvPhoneNumber=itemView.findViewById<TextView>(R.id.tv_party_phoneno)
        val tvEmail=itemView.findViewById<TextView>(R.id.tv_employee_email)
        companion object{
            fun createViewHolder(parent: ViewGroup): EmployeeItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.employee_list_item, parent, false)
                return EmployeeItem(view)
            }
        }
        fun bind(employee:Employee) {
            val employeeName=employee.name
            val phoneNumber=employee.phoneNo
            val email=employee.emailId

            tvEmployeeName.text=employeeName
            tvPhoneNumber.text=phoneNumber
            Linkify.addLinks(tvPhoneNumber,Linkify.ALL)
            tvEmail.text=email
        }
    }

}