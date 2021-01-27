package com.example.salestracking.leave

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.R
import com.example.salestracking.databse.model.Leave

class LeaveListAdapter(var LeaveList: List<Leave>): RecyclerView.Adapter<LeaveListAdapter.LeaveItem>() {
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
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${LeaveList.size}")
        return LeaveList.size

    }
    class LeaveItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvDate=itemView.findViewById<TextView>(R.id.tv_date)
        val tvLeaveType=itemView.findViewById<TextView>(R.id.tv_leave_type)
        val tvreason=itemView.findViewById<TextView>(R.id.tv_reason)
        val tvStatus=itemView.findViewById<TextView>(R.id.tv_status)
        companion object{
            fun createViewHolder(parent: ViewGroup): LeaveItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.leave_list_item, parent, false)
                return LeaveItem (view)
            }
        }
        fun bind(leave: Leave) {
            val date=leave.fromdate+" to "+leave.toDate
            val leaveType=leave.leaveType
            val reason=leave.reason
            val status=leave.status
            tvDate.text=date
            tvLeaveType.text=leaveType
            tvreason.text=reason
            tvStatus.text=status

        }
    }
}