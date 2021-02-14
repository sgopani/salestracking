package com.example.salesadmin.admin

import android.graphics.Color
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salesadmin.R
import com.example.salesadmin.model.Employee
import com.example.salesadmin.model.Party
import java.util.*


class EmployeeListAdapter(var employeeList: MutableList<Employee>): RecyclerView.Adapter<EmployeeListAdapter.EmployeeItem>() {
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
        val generator:ColorGenerator= ColorGenerator.MATERIAL
        val color: Int = generator.randomColor
        val employee:Employee=getItem(position)
        holder.bind(employee)
        val drawable =
            TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                .buildRound(employee.name[0].toString().toUpperCase(Locale.ROOT), color)
        holder.employeeImage.setImageDrawable(drawable)
    }
    class EmployeeItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvEmployeeName=itemView.findViewById<TextView>(R.id.tv_notification_message)
        val tvPhoneNumber=itemView.findViewById<TextView>(R.id.tv_party_phoneno)
        val tvEmail=itemView.findViewById<TextView>(R.id.tv_employee_email)
        val employeeImage=itemView.findViewById<ImageView>(R.id.employeeImage)
        companion object{
            fun createViewHolder(parent: ViewGroup): EmployeeItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.employee_list_item, parent, false)
                return EmployeeItem(view)
            }
        }
        fun bind(employee: Employee) {
            val employeeName=employee.name
            val phoneNumber=employee.phoneNo
            val email=employee.emailId

            tvEmployeeName.text=employeeName
            tvPhoneNumber.text=phoneNumber
            Linkify.addLinks(tvPhoneNumber, Linkify.ALL)
            tvEmail.text=email
        }
    }
    fun updateList(list: MutableList<Employee>){
        employeeList=list
        notifyDataSetChanged()
    }

}