package com.example.salestracking.orders

import android.app.AlertDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.AddToCartItemClickListener
import com.example.salestracking.PrefManager
import com.example.salestracking.R
import com.example.salestracking.databse.model.*
import com.example.salestracking.repository.FireStoreRepository
import com.example.salestracking.repository.FireStoreViewModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CartList : Fragment() {
    private lateinit var rootView: View
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CartItemAdapter
    private var cartList:MutableList<CartItem> = ArrayList()
    private var partyList:Party? = null
    private  lateinit var fireStoreRepository:FireStoreRepository
    private lateinit var total:TextView
    private lateinit var placeOrderBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel= ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.mutableCart.observe(viewLifecycleOwner, Observer {
            //Log.d("mutableCart", "${it.size}")
            if(it!=null){
                cartList = it
                adapter.cartList=cartList
                placeOrderBtn.isClickable = cartList.size != 0
            }

        })
        viewModel.getTotalPrice().observe(viewLifecycleOwner, Observer {
//            Toast.makeText(context,"$it",Toast.LENGTH_SHORT).show()
            if(it!=null){
                total.text = getString(R.string.total,it.toString())
            }

        })
    }

    private fun init(){
        recyclerView=rootView.findViewById(R.id.rv_cart_item)
        fireStoreRepository= FireStoreRepository()
        total=rootView.findViewById(R.id.tv_total_price)
        placeOrderBtn=rootView.findViewById(R.id.btn_place_order)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.cart_item, container, false)
        init()
        configureProductList()
        partyList=CartListArgs.fromBundle(requireArguments()).party
        //Toast.makeText(this.context,"$partyList",Toast.LENGTH_SHORT).show()
        //Log.d("getCart()","${cartList.toTypedArray().size}")
        adapter.cartList=cartList
        adapter.notifyDataSetChanged()

//        viewModel.getCart().observe(this.requireActivity(), Observer {cartItemListviewmodel->
//
////            cartList=cartItemListviewmodel
////            adapter.cartList=cartItemListviewmodel
////            adapter.notifyDataSetChanged()
//        })
        placeOrderBtn.setOnClickListener {

            if(cartList.isEmpty()){
                Toast.makeText(context,"Please add item in the cart",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else{
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    placeOrder()
                }
            }


        }
        return rootView
    }
    private fun configureProductList(){
        adapter= CartItemAdapter(cartList,getNewsItemClickListener())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)

    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun placeOrder(){
        val prefManager: PrefManager = PrefManager(this.requireContext())
        val uid=viewModel.getEmployeeUid()
        val currentTime=Calendar.getInstance().timeInMillis
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure you want to Place the order")
//                    setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
                if(partyList!=null){
                    //val productList=ProductList(cartList)
                    //val hashMap = hashMapOf<String,ArrayList<CartItem>>()
                    //val hashMap= ArrayList<HashMap<String,CartItem>>()
                    val order=Order(
                        partyList!!,cartList,total.text.toString(),currentTime.toString()
                        ,prefManager.getFullName().toString(),uid)
                    viewModel.placeOrder(order)
                    viewModel.clear()
                    val action = CartListDirections.actionCartItemToOrderSuccessful()
                    findNavController().navigate(action)
                }
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }
    private fun getNewsItemClickListener(): AddToCartItemClickListener {
        return object : AddToCartItemClickListener {
            override fun onProductItemClick(products: Products) {

            }
            override fun deleteCartItem(cartItem: CartItem) {
                //Log.d("getCart()", cartItem.products.productName)
                viewModel.removeItemFromCart(cartItem)
                //adapter.cartList.remove(cartList)
                //adapter.notifyDataSetChanged()
                cartList.remove(cartItem)
                adapter.notifyDataSetChanged()
            }

            override fun changeQuantity(cartItem: CartItem, quantity: Int) {
                viewModel.changeQuantity(cartItem,quantity)
            }
        }
    }

}