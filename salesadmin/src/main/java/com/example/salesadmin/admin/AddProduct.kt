package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.salesadmin.CheckNetClass
import com.example.salesadmin.R
import com.example.salesadmin.model.Products
import com.example.salesadmin.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class AddProduct : Fragment() {
    private lateinit var rootView: View
    private lateinit var productName:EditText
    private lateinit var productPrice:EditText
    private lateinit var productQuantity:EditText
    private lateinit var addProduct:Button
    private lateinit var fstore: FirebaseFirestore
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var user:FirebaseUser
    private lateinit var progressBar: ProgressBar
    private var valid:Boolean=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun init() {
        productName=rootView.findViewById(R.id.et_product_name)
        productPrice=rootView.findViewById(R.id.et_product_Price)
        productQuantity=rootView.findViewById(R.id.et_product_description)
        addProduct=rootView.findViewById(R.id.bt_add_product)
        fstore= FirebaseFirestore.getInstance()
        progressBar=rootView.findViewById(R.id.progress_bar)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         rootView=inflater.inflate(R.layout.add_product, container, false)
        init()

        addProduct.setOnClickListener {
            checkField(productName)
            checkField(productPrice)
            checkField(productQuantity)
            if (CheckNetClass.checknetwork(this.requireContext())) {

            if (valid) {
                addProduct.isClickable=false
                progressBar.visibility = View.VISIBLE
                checkIfProductExist()
            } else {
                Toast.makeText(this.requireContext(), "Please enter fields properly", Toast.LENGTH_SHORT).show()
            }
        }
            else{
                Toast.makeText(this.requireContext(),"Sorry,no internet connectivty",Toast.LENGTH_LONG).show();
            }
        }
        return rootView
    }
    private  fun checkIfProductExist(){
        user= FirebaseAuth.getInstance().currentUser!!
        val documentReference=fstore.collection("Sales").document(user?.uid)
            .collection("Products").document(productName.text.toString())
        documentReference.get().addOnSuccessListener {document->
            if(document.exists()){
                addProduct.isClickable=true
                progressBar.visibility=View.GONE
                Toast.makeText(this.requireContext(),"Product With this name already exist",Toast.LENGTH_SHORT).show()
            }
            else{
                addProduct()
            }

        }
        }

    private fun addProduct() {
        viewModel= FireStoreViewModel()
        val time=System.currentTimeMillis()
        val products=Products(productName.text.toString(),productPrice.text.toString()
                ,productQuantity.text.toString(),productName.text.toString(),time)
        viewModel.addProductFirebase(products)
        progressBar.visibility=View.GONE
        addProduct.isClickable=true
            Toast.makeText(this.requireContext()," Product added Successfully",Toast.LENGTH_SHORT).show()
            val action=AddProductDirections.actionAddProductToProductsList()
            findNavController().navigate(action)
    }
    private fun checkField(textField: EditText): Boolean {
        when {
            textField.text.toString().isEmpty() -> {
                textField.error = "Field cannot be empty"
                valid = false
            }
            productPrice.text.toString()<= 0.toString() ->{
                productPrice.error="Price Cannot be less than zero"
                valid=false

            }
            productQuantity.text.toString()<= 0.toString()->{
                productQuantity.error="Quantity Cannot be less than zero"
                valid=false
            }

            else->{
                valid=true
            }
        }
        return valid
    }

}