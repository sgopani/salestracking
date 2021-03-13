package com.example.salestracking.orders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.OrderDetailsItemClickListener
import com.example.salestracking.R
import com.example.salestracking.databse.model.Order
import com.example.salestracking.toSimpleDateFormat

class OrderListAdapter (var OrderList: MutableList<Order>,var orderDetailsItemClickListener: OrderDetailsItemClickListener): RecyclerView.Adapter<OrderListAdapter.OrderItem>() {
    //    private var productList= mutableListOf<Products>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderItem {
        return OrderItem.createViewHolder(parent)
    }
    private fun getItem(position: Int): Order {
        return OrderList[position]
    }

    override fun onBindViewHolder(holder:OrderItem , position: Int) {
        val order=getItem(position)
        holder.bind(order)
        holder.tvViewDetails.setOnClickListener {
            orderDetailsItemClickListener.onOrderItemClick(order)
        }
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${OrderList.size}")
        return OrderList.size

    }
//    fun getCollectionPosition(Position:Int):Long{
//        return OrderList[Position].time
//    }
    fun remove(position: Int){
        OrderList.removeAt(position)
        notifyItemRemoved(position)
    }
    class OrderItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvDate = itemView.findViewById<TextView>(R.id.tv_order_date)
        val tvPartyName = itemView.findViewById<TextView>(R.id.tv_order_party_name)
        val tvAmount = itemView.findViewById<TextView>(R.id.tv_order_amount)
        val tvViewDetails=itemView.findViewById<TextView>(R.id.tv_view_details)

        companion object {
            fun createViewHolder(parent: ViewGroup): OrderItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.order_list_item, parent, false)
                return OrderItem(view)
            }
        }

        fun bind(order: Order) {
            val dateConvert=toSimpleDateFormat(order.time.toLong())
            val date=dateConvert
            val partyName= order.party?.name
            val totalAmount=order.total
            tvDate.text=date
            tvPartyName.text=partyName
            tvAmount.text=totalAmount
        }
    }

}
//            val date=leave.fromdate+" to "+leave.toDate
//            val leaveType=leave.leaveType
//            val reason=leave.reason
//            val status=leave.status
//            tvDate.text=date
//            tvLeaveType.text=leaveType
//            tvreason.text=reason
//            tvStatus.text=status
//            if(tvStatus.text=="Accepted"){
//                tvStatus.setTextColor(ContextCompat.getColor(this.itemView.context, R.color.green))
//            }
//            else{
//                tvStatus.setTextColor(ContextCompat.getColor(this.itemView.context, R.color.colorRed))
//            }
//
//        }

//    fun updateList(list: MutableList<Leave>){
//        OrderList=list
//        notifyDataSetChanged()
//    }