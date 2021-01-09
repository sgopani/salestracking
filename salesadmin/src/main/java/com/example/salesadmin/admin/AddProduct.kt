package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.salesadmin.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class AddProduct : Fragment() {
    private lateinit var rootView: View
    private lateinit var productName:EditText
    private lateinit var productPrice:EditText
    private lateinit var productQuantity:EditText
    private lateinit var addProduct:Button
    private lateinit var fstore: FirebaseFirestore
    private var valid:Boolean=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    fun init() {
        productName=rootView.findViewById(R.id.et_product_name)
        productPrice=rootView.findViewById(R.id.et_product_Price)
        productQuantity=rootView.findViewById(R.id.et_product_quantity)
        addProduct=rootView.findViewById(R.id.bt_add_product)
        fstore= FirebaseFirestore.getInstance()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
         rootView=inflater.inflate(R.layout.add_product, container, false)
        init()
        addProduct.setOnClickListener{
            checkField(productName)
            checkField(productPrice)
            checkField(productQuantity)
            if(valid){
                addProduct()
            }
            else{
                Toast.makeText(this.requireContext(),"Please enter fields properly", Toast.LENGTH_SHORT).show()
            }
        }
        return rootView
    }

    private fun addProduct() {
        val df: DocumentReference = fstore.collection("Sales").document("admin").collection("Products").document(
            productName.text.toString())
        val productInfo= mutableMapOf<String, String>()
        productInfo[" Product name"] = productName.text.toString()
        productInfo[" Product price"] = productPrice.text.toString()
        productInfo[" Product quantity"] = productQuantity.text.toString()
        productInfo["Product Id"]=df.id
        df.set(productInfo).addOnSuccessListener {
            Toast.makeText(this.requireContext()," Added Successful",Toast.LENGTH_SHORT).show()
            val action=AddProductDirections.actionAddProductToAdminDashboard()
            findNavController().navigate(action)
        }.addOnFailureListener {
            Toast.makeText(this.requireContext()," Unable to add",Toast.LENGTH_SHORT).show()
        }

        Toast.makeText(this.requireContext(),"Successful",Toast.LENGTH_SHORT).show()
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