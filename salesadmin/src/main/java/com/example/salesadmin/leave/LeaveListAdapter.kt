package com.example.salesadmin.leave

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.LeaveItemClickListener
import com.example.salesadmin.R
import com.example.salesadmin.model.Leave
import com.example.salesadmin.model.Products

class LeaveListAdapter(var LeaveList: List<Leave>,var leaveItemClickListeners:LeaveItemClickListener):
    RecyclerView.Adapter<LeaveListAdapter.LeaveItem>() {
    //    private var productList= mutableListOf<Products>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeaveItem {
        return LeaveItem.createViewHolder(parent)
    }
    private fun getItem(position: Int):Leave {
        return LeaveList[position]
    }

    override fun onBindViewHolder(holder: LeaveItem, position: Int) {
        val leave=getItem(position)
        holder.bind(leave)
        holder.itemView.setOnClickListener{
            leaveItemClickListeners.onLeaveItemClick(leave)
        }
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${LeaveList.size}")
        return LeaveList.size

    }
    class LeaveItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvname=itemView.findViewById<TextView>(R.id.tv_employee_name)
        val tvDate=itemView.findViewById<TextView>(R.id.tv_leave_date)
        val tvStatus=itemView.findViewById<TextView>(R.id.tv_status)
        companion object{
            fun createViewHolder(parent: ViewGroup): LeaveItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.leave_list_item, parent, false)
                return LeaveItem (view)
            }
        }
        fun bind(leave: Leave) {
            val name=leave.name
            val date=leave.fromdate+" to "+leave.toDate
            val status=leave.status
            tvname.text=name
            tvDate.text=date
            tvStatus.text=status
            if(tvStatus.text=="Accepted"){
                tvStatus.setTextColor(ContextCompat.getColor(this.itemView.context,R.color.green))
            }
            else{
                tvStatus.setTextColor(ContextCompat.getColor(this.itemView.context,R.color.colorRed))
            }
        }
    }
    fun updateList(list: List<Leave>){
        LeaveList=list
        notifyDataSetChanged()
    }
}