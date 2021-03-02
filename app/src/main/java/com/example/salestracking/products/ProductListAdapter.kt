package com.example.salestracking.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.AddToCartItemClickListener
import com.example.salestracking.R
import com.example.salestracking.databse.model.Products


class ProductListAdapter( var productList: MutableList<Products>,var addToCartItemClickListener: AddToCartItemClickListener)
    : RecyclerView.Adapter<ProductListAdapter.ProductItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductItem {
        return ProductItem.createViewHolder(parent)
    }
    private fun getItem(position: Int): Products {
        return productList[position]
    }

    override fun onBindViewHolder(holder: ProductItem, position: Int) {
        val products=getItem(position)
        holder.bind(products)
        holder.tvAddToCart.setOnClickListener {
            addToCartItemClickListener.onProductItemClick(products)
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
        val tvPrice=itemView.findViewById<TextView>(R.id.text_view_price)
        val tvAddToCart=itemView.findViewById<TextView>(R.id.tv_add_to_cart)
        companion object{
            fun createViewHolder(parent: ViewGroup): ProductItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.order_product_list_item, parent, false)
                return ProductItem(view)
            }
        }
        fun bind(products: Products) {
            val productName=products.productName
            //val description=products.productDescription
            val price=products.productPrice
            tvProductName.text=productName
            //tvDescription.text=description
            tvPrice.text=price

        }
    }
}