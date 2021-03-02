package com.example.salestracking.orders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.*
import com.example.salestracking.databse.model.Party
import com.example.salestracking.databse.model.Products
import com.example.salestracking.products.ProductListAdapter
import com.example.salestracking.products.ProductsListDirections
import com.example.salestracking.repository.FireStoreViewModel
import java.util.ArrayList

class AddOrders : Fragment() {
    private lateinit var rootView:View
    private var partyDetails:Party?=null
    private lateinit var addParty:EditText
    private lateinit var adapter: ProductListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noProduct: TextView
    private var productList: MutableList<Products> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    private fun init(){
        recyclerView = rootView.findViewById(R.id.rv_order_product_list)
        noProduct=rootView.findViewById(R.id.no_product)
        progressBar = rootView.findViewById(R.id.progress_bar)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView=inflater.inflate(R.layout.add_orders, container, false)
        init()
        addParty=rootView.findViewById(R.id.et_add_party)
        partyDetails = AddOrdersArgs.fromBundle(requireArguments()).party
        //Toast.makeText(context,"${partyDetails.name}",Toast.LENGTH_LONG).show()
        if(partyDetails!=null){
            addParty.setText(partyDetails?.name)
            //Toast.makeText(context, "${viewModel.selectedOrderParty.value?.name}",Toast.LENGTH_LONG).show()
            //addParty.setText(viewModel.selectedOrderParty.value?.name)
        }
        configureProductList()
        progressBar.visibility = View.VISIBLE

        viewModel = FireStoreViewModel()
        viewModel.getAllProducts()
        viewModel.productList.observe(this.requireActivity(), Observer { products ->
            //Log.d("loadData1","${viewModel.productList.value}")
            noProduct.visibility=View.GONE
            productList = products
            progressBar.visibility=View.GONE
            adapter.productList = productList
            adapter.notifyDataSetChanged()
        })
        viewModel.selectedOrderProduct.observe(this.viewLifecycleOwner, Observer { productList->
            if (productList != null) {
                val action= ProductsListDirections.actionProductsListToAddOrders(party = null,productList)
                findNavController().navigate(action)
                //viewModel.eventNavigateProductDetailCompleted()
            }
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkStatus(status)
        })
        viewModel.getCart().observe(this.requireActivity(), Observer {
            Log.d("getCart()","${it.size}")
        })
//        addProductBtn.setOnClickListener {
//            val action = ProductsListDirections.actionProductsListToAddProduct()
//            findNavController().navigate(action)
//        }
        //loadData()
        return rootView
    }
    private fun configureProductList(){
        adapter = ProductListAdapter(productList,getNewsItemClickListener())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        //itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun getNewsItemClickListener(): AddToCartItemClickListener {
        return object : AddToCartItemClickListener {
            override fun onProductItemClick(products: Products) {
                Log.d("onLeaveItemClick","$products")
                viewModel.addProductToCart(products)
                //viewModel.eventNavigateToProductDetail(products)
                //viewModel.eventNavigateToOderProductList(products)
            }
        }
    }
    private fun checkStatus(status: SalesApiStatus) {
    when (status) {
        SalesApiStatus.LOADING -> {
            progressBar.visibility=View.VISIBLE
        }
        SalesApiStatus.ERROR -> {
            if (isInternetOn(this.requireContext())) {
                Toast.makeText(this.context, "Connected to internet", Toast.LENGTH_SHORT).show()
                //findNavController().navigate(R.id.newsList2)
            } else {
                Toast.makeText(this.context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
            }
            progressBar.visibility = View.GONE

        }
        SalesApiStatus.DONE -> {
            noProduct.visibility=View.INVISIBLE
            progressBar.visibility = View.GONE
        }
        SalesApiStatus.EMPTY->{
            noProduct.text=getString(R.string.no_product)
            noProduct.visibility=View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}

//    private lateinit var rootView:View
//    private lateinit var addParty:EditText
//    private lateinit var date:EditText

//    private var productDetails:Products?=null
//    private lateinit var addProducts: Button
//    private lateinit var viewModel: FireStoreViewModel
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//    }
//    private fun init(){
//        addParty=rootView.findViewById(R.id.add_order_party)
//        date=rootView.findViewById(R.id.et_order_date)
//        date.setText(toSimpleDateFormat(System.currentTimeMillis()))
//        addProducts=rootView.findViewById(R.id.btn_add_product_orders)
//        viewModel= FireStoreViewModel()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Toast.makeText(context,"onDestroy()",Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Toast.makeText(context,"onPause()",Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Toast.makeText(context,"onResume()",Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        Toast.makeText(context,"onDestroyView()",Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//        // Inflate the layout for this fragment
//        rootView=inflater.inflate(R.layout.add_orders, container, false)
//        init()
//        Toast.makeText(context,"onCreateView",Toast.LENGTH_SHORT).show()
//        //Toast.makeText(context, "${partyDetails?.name}", Toast.LENGTH_LONG).show()
//        viewModel.selectedOrderParty.observe(this.viewLifecycleOwner, Observer { partyList->
//            //partyDetails=partyList
//
//            addParty.setText(partyList.name)
//            Toast.makeText(context, "${partyList?.name}", Toast.LENGTH_LONG).show()
//                //viewModel.selectedOrderParty.value=partyList
//            //partyDetails=partyList
//
//        })
//        partyDetails = AddOrdersArgs.fromBundle(requireArguments()).party
//        //Toast.makeText(context,"${partyDetails.name}",Toast.LENGTH_LONG).show()
//        if(partyDetails!=null){
//            addParty.setText(partyDetails?.name)
//            Toast.makeText(context, "${viewModel.selectedOrderParty.value?.name}",Toast.LENGTH_LONG).show()
//            //addParty.setText(viewModel.selectedOrderParty.value?.name)
//        }
//        productDetails=AddOrdersArgs.fromBundle(requireArguments()).products
//        if(productDetails!=null){
//            Toast.makeText(context, "$productDetails", Toast.LENGTH_LONG).show()
//            addProducts.text = productDetails!!.productName
//        }
//
//        viewModel.selectedOrderProduct.observe(this.viewLifecycleOwner, Observer { partyList->
//            //partyDetails=partyList
//
//            addProducts.text = partyList.productName
//            //partyDetails=partyList
//
//        })
//
//        addParty.setOnClickListener(this)
//        addProducts.setOnClickListener(this)
//
//        return rootView
//
//    }
//
//
//    override fun onClick(view: View?) {
//        when(view?.id){
//            R.id.add_order_party->{
//                requestCode =0
//                val tag="String"
//                val action=AddOrdersDirections.actionAddOrdersToPartyList()
//                findNavController().navigate(action)
//            }
//            R.id.btn_add_product_orders->{
////                val fragment=ProductsList()
////                val fragmentTransaction= activity?.supportFragmentManager?.beginTransaction()
////                fragmentTransaction?.add(R.id.nav_host_fragment_container,fragment)
////                fragmentTransaction?.addToBackStack(null);
////                fragmentTransaction?.commit()
//                //childFragmentManager.beginTransaction()?.add(R.id.container, fragment)?.commit()
//                val action=AddOrdersDirections.actionAddOrdersToProductsList()
//                findNavController().navigate(action)
//            }
//        }
//    }
}