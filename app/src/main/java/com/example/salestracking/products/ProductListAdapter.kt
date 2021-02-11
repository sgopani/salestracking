package com.example.salestracking.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.ProductItemClickListener
import com.example.salestracking.R
import com.example.salestracking.databse.model.Products

class ProductListAdapter( var productList: MutableList<Products>,var productItemClickListener: ProductItemClickListener): RecyclerView.Adapter<ProductListAdapter.ProductItem>() {
//    private var productList= mutableListOf<Products>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItem {
        return ProductItem.createViewHolder(parent)
    }
    private fun getItem(position: Int): Products {
        return productList[position]
    }

    override fun onBindViewHolder(holder: ProductItem, position: Int) {
        val products=getItem(position)
        holder.bind(products)
        holder.itemView.setOnClickListener {
            productItemClickListener.onProductItemClick(products)
        }
    }
//    fun submitList(list:MutableList<Products>){
//        productList = list
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${productList.size}")
        return productList.size

    }
    fun getProductPosition(Position:Int):String{
        return productList[Position].productName
    }
    fun remove(position: Int){
        productList.removeAt(position)
        notifyItemRemoved(position)
    }
    class ProductItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvProductName=itemView.findViewById<TextView>(R.id.text_view_title)
        val tvQuantity=itemView.findViewById<TextView>(R.id.text_view_quantity)
        val tvPrice=itemView.findViewById<TextView>(R.id.text_view_price)
        companion object{
            fun createViewHolder(parent: ViewGroup): ProductItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_list_item, parent, false)
                return ProductItem(view)
            }
        }
        fun bind(products: Products) {
            val productName=products.productName
            val quantity=products.productQuantity
            val price=products.productPrice
            tvProductName.text=productName
            tvQuantity.text=quantity
            tvPrice.text=price
        }
    }
}