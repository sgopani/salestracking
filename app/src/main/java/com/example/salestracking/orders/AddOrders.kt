package com.example.salestracking.orders

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.AddToCartItemClickListener
import com.example.salestracking.R
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.CartItem
import com.example.salestracking.databse.model.Party
import com.example.salestracking.databse.model.Products
import com.example.salestracking.isInternetOn
import com.example.salestracking.products.ProductListAdapter
import com.example.salestracking.products.ProductsListDirections
import com.example.salestracking.repository.FireStoreViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*


class AddOrders : Fragment() {
    private lateinit var rootView: View
    private var partyDetails: Party? = null
    private lateinit var addParty: EditText
    private lateinit var adapter: ProductListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noProduct: TextView
    private var productList: MutableList<Products> = ArrayList()
    private var cartList: MutableList<CartItem> = ArrayList()
    private var cartQuantity: Int = 0
//    private lateinit var fireStoreRepository:FireStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        viewModel.mutableCart.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Log.d("Cart()", "${it.size}")
                cartList = it
                var quantity = 0
                for (cartItem in it) {
                    quantity += cartItem.quantity
                }
                cartQuantity = quantity
                activity?.invalidateOptionsMenu()
            }
        })

    }

    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_order_product_list)
        noProduct = rootView.findViewById(R.id.no_product)
        progressBar = rootView.findViewById(R.id.progress_bar)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(R.layout.add_orders, container, false)
        init()
        addParty = rootView.findViewById(R.id.et_add_party)
        //Toast.makeText(context, "${cartList.size}", Toast.LENGTH_SHORT).show()
        partyDetails = AddOrdersArgs.fromBundle(requireArguments()).party
        //Toast.makeText(context,"${partyDetails.name}",Toast.LENGTH_LONG).show()
        if (partyDetails != null) {
            addParty.setText(partyDetails?.name)
            //Toast.makeText(context, "${viewModel.selectedOrderParty.value?.name}",Toast.LENGTH_LONG).show()
            //addParty.setText(viewModel.selectedOrderParty.value?.name)
        }
        configureProductList()
        progressBar.visibility = View.VISIBLE

        viewModel.getAllProducts()
        viewModel.productList.observe(this.viewLifecycleOwner, Observer { products ->
            //Log.d("loadData1","${viewModel.productList.value}")
            noProduct.visibility = View.GONE
            productList = products
            progressBar.visibility = View.GONE
            adapter.productList = productList
            adapter.notifyDataSetChanged()
        })

        viewModel.selectedOrderProduct.observe(this.viewLifecycleOwner, Observer { productList ->
            if (productList != null) {
                val action = ProductsListDirections.actionProductsListToAddOrders(
                        party = null,
                        productList
                )
                findNavController().navigate(action)
            }
        })

        viewModel.status.observe(this.viewLifecycleOwner, Observer { status ->
            checkStatus(status)
        })
        this.setHasOptionsMenu(true)
        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.cart_menu, menu)
        val menuItem = menu.findItem(R.id.cartFragment)
        val actionView: View = menuItem.actionView
        val cartBadgeQuantity = actionView.findViewById<TextView>(R.id.cart_badge_text_view)
        cartBadgeQuantity.text = cartQuantity.toString()
        val zero = 0
        if (cartBadgeQuantity.text.toString() == zero.toString()) {
            cartBadgeQuantity.visibility = View.GONE
        }
        actionView.setOnClickListener {
            onOptionsItemSelected(menuItem)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.cartFragment) {
            val action = AddOrdersDirections.actionAddOrdersToCartItem(partyDetails)
            findNavController().navigate(action)
            //Toast.makeText(context, "${cartList.size}", Toast.LENGTH_SHORT).show()
            return true
        }
        return false

    }

    private fun configureProductList() {
        adapter = ProductListAdapter(productList, getNewsItemClickListener())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        //itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun getNewsItemClickListener(): AddToCartItemClickListener {
        return object : AddToCartItemClickListener {
            override fun onProductItemClick(products: Products) {
                Log.d("onLeaveItemClick", "$products")
                val isAdded = viewModel.addProductToCart(products)
                if (isAdded) {
                    Snackbar.make(
                            requireView(),
                            products.productName.toString() + " added to cart.",
                            Snackbar.LENGTH_LONG
                    )
                            .setAction("Checkout") {
                                val action = AddOrdersDirections.actionAddOrdersToCartItem(partyDetails)
                                findNavController().navigate(action)
                            }
                            .show()
                } else {
                    Snackbar.make(
                            requireView(),
                            "Already have the max quantity in cart.",
                            Snackbar.LENGTH_LONG
                    )
                            .show()
                }

                //viewModel.eventNavigateToProductDetail(products)
                //viewModel.eventNavigateToOderProductList(products)
            }

            override fun deleteCartItem(cartItem: CartItem) {
            }

            override fun changeQuantity(cartItem: CartItem, quantity: Int) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun checkStatus(status: SalesApiStatus) {
        when (status) {
            SalesApiStatus.LOADING -> {
                progressBar.visibility = View.VISIBLE
            }
            SalesApiStatus.ERROR -> {
                if (isInternetOn(this.requireContext())) {
                    Toast.makeText(this.context, "Connected to internet", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.newsList2)
                } else {
                    Toast.makeText(
                            this.context,
                            "Please Check Your Internet Connection",
                            Toast.LENGTH_SHORT
                    ).show()
                }
                progressBar.visibility = View.GONE

            }
            SalesApiStatus.DONE -> {
                noProduct.visibility = View.INVISIBLE
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY -> {
                noProduct.text = getString(R.string.no_product)
                noProduct.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
}