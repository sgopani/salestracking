package com.example.salestracking.orders

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.AddToCartItemClickListener
import com.example.salestracking.COMPANYUID
import com.example.salestracking.R
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.CartItem
import com.example.salestracking.databse.model.Order
import com.example.salestracking.databse.model.Products
import kotlinx.coroutines.launch

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
                notifyDataSetChanged()
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

                    val quantity=position+1
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
                val productName = cart.products?.productName
                val price = cart.products?.productPrice!!.toInt() *cart.quantity
                val quantity=cart.quantity
                tvproductName.text=productName
                tvPrice.text=price.toString()
                spinnerQuantity.setSelection(quantity-1)

            }
        }


    }

//fun getAllOrderList(){
//    coroutineScope.launch {
//        val orderList = fstore.collection("Sales")
//            .document(COMPANYUID).collection("Order")
////                .whereEqualTo(
////                    "employeeUid",
////                    user?.uid.toString()
////                ).orderBy("time", Query.Direction.DESCENDING)
//        orderList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
////                try {
//            _status.value = SalesApiStatus.LOADING
//            if (querySnapshot?.isEmpty!!) {
//                _status.value = SalesApiStatus.EMPTY
//                //Log.d(TAG, firebaseFirestoreException?.message.toString())
//
//            } else {
//                for(document in querySnapshot){
//                    for ((key,value) in document.data){
//                        if(key=="productlist"){
//                            val splitAndAddToList=value.toString().drop(1).dropLast(1)
//                            val productList=splitAndAddToList.split(",").toTypedArray().toList()
//                            Log.d(TAG, "$productList")
//                        }
//                    }
////                            val order=document.toObject(Order::class.java)
////                            Log.d(TAG,"$order")
//                    //Log.i(TAG,"${order.productList2}")
//                    //Log.d(TAG, "${document.id} => ${document.data}")
////                            _mapList.value=document.data
////                            if(_mapList.value!=null) {
////                                for (productlist in _mapList.value?.keys!!) {
////                                    //Log.d(TAG, "$productlist => ${_mapList.value!!["productlist"]}")
//////                                    order.productList2.put("productlist",
//////                                        _mapList.value!!["productlist"] as CartItem
//////                                    )
////                                   //Log.d(TAG,"${order.productList2}")
////                                }
////                            }
//                }
//                val innerEvents = querySnapshot.toObjects(Order::class.java)
//                //val Order:Order=querySnapshot.
//                _orderList.postValue(innerEvents)
//                //Log.i("orderList()1","${_orderList.value}")
//                _status.value = SalesApiStatus.DONE
//            }
//
////                } catch (t: Throwable) {
////                    Log.d(TAG, firebaseFirestoreException?.message.toString())
////                    _status.value = SalesApiStatus.ERROR
////                }
//
//        }
//    }
//}