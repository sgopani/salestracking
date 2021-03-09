package com.example.salesadmin.orders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.R
import com.example.salesadmin.model.CartItem

class OrderDetailsAdapter(var CartList: MutableList<CartItem>)
    : RecyclerView.Adapter<OrderDetailsAdapter.OrderDetailItem>() {
    //    private var productList= mutableListOf<Products>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderDetailItem {
        return OrderDetailItem.createViewHolder(parent)
    }
    private fun getItem(position: Int): CartItem {
        return CartList[position]
    }

    override fun onBindViewHolder(holder:OrderDetailItem, position: Int) {
        val order=getItem(position)
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${CartList.size}")
        return CartList.size

    }
    //    fun getCollectionPosition(Position:Int):Long{
//        return OrderList[Position].time
//    }
//    fun remove(position: Int){
//        OrderList.removeAt(position)
//        notifyItemRemoved(position)
//    }
    class OrderDetailItem(itemView: View): RecyclerView.ViewHolder(itemView) {
        //val tvDate = itemView.findViewById<TextView>(R.id.tv_order_date)
        val tvProductsName = itemView.findViewById<TextView>(R.id.tv_order_detail_product_name)
        val tvproductQuantity=itemView.findViewById<TextView>(R.id.tv_order_details_quantity)

        //val tvAmount = itemView.findViewById<TextView>(R.id.tv_order_amount)

        companion object {
            fun createViewHolder(parent: ViewGroup): OrderDetailItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.order_detail_list_item, parent, false)
                return OrderDetailItem(view)
            }
        }

        fun bind(cartItem: CartItem) {
            //val date=
            val partyName= cartItem.products?.productName
            val quantity=cartItem.quantity
            //val totalAmount=order.total
            //tvDate.text=date
            tvProductsName.text=partyName
            tvproductQuantity.text=quantity.toString()
            //tvAmount.text=totalAmount

        }
    }
}