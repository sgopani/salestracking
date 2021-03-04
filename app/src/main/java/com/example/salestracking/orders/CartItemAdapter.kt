package com.example.salestracking.orders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.AddToCartItemClickListener
import com.example.salestracking.R
import com.example.salestracking.databse.model.CartItem

class CartItemAdapter (var cartList: MutableList<CartItem>,
                       var addToCartItemClickListener: AddToCartItemClickListener) : RecyclerView.Adapter<CartItemAdapter.Cart>() {
        //    private var productList= mutableListOf<Products>()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Cart {
            return Cart.createViewHolder(parent)
        }

        private fun getItem(position: Int):CartItem {
            return cartList[position]
        }

        override fun onBindViewHolder(holder:Cart, position: Int) {
            val cartItems = getItem(position)
            holder.bind(cartItems)
            holder.deleteButton.setOnClickListener {
                addToCartItemClickListener.deleteCartItem(cartItems)
            }
            holder.spinnerQuantity.onItemSelectedListener=object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    var quantity=position+1
                    if(quantity==cartItems.quantity){
                        return
                    }
                    addToCartItemClickListener.changeQuantity(cartItems,quantity)
                    notifyDataSetChanged()
                }

            }
        }

        override fun getItemCount(): Int {
            Log.d("getItemCount", "${cartList.size}")
            return cartList.size

        }

        class Cart(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvproductName = itemView.findViewById<TextView>(R.id.tv_product_name)
            val tvPrice = itemView.findViewById<TextView>(R.id.tv_product_price)
            val spinnerQuantity=itemView.findViewById<Spinner>(R.id.quantitySpinner)
            val deleteButton=itemView.findViewById<ImageView>(R.id.iv_delete_button)
            companion object {
                fun createViewHolder(parent: ViewGroup): Cart{
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.cart_list_item ,parent, false)
                    return Cart(view)
                }
            }

            fun bind(cart:CartItem) {
                val productName = cart.products.productName
                val price = cart.products.productPrice.toInt() *cart.quantity
                val quantity=cart.quantity
                tvproductName.text=productName
                tvPrice.text=price.toString()
                spinnerQuantity.setSelection(quantity-1)

            }
        }


    }