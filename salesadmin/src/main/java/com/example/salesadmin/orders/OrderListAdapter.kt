package com.example.salesadmin.orders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.OrderDetailsItemClickListener
import com.example.salesadmin.R
import com.example.salesadmin.model.Order
import com.example.salesadmin.toSimpleDateFormat

class OrderListAdapter (var OrderList: MutableList<Order>,
                        var orderDetailsItemClickListener: OrderDetailsItemClickListener
): RecyclerView.Adapter<OrderListAdapter.OrderItem>() {
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
        holder.itemView.setOnClickListener {
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
        val tvEmployeeName=itemView.findViewById<TextView>(R.id.tv_order_employee_name)


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
            val employeeName=order.employeename
            tvDate.text=date
            tvPartyName.text=partyName
            tvAmount.text=totalAmount
            tvEmployeeName.text=employeeName
        }
    }

}