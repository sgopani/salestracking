package com.example.salesadmin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.salesadmin.model.Products
import com.example.salesadmin.model.RegisterAdmin
import com.example.salesadmin.register.RegisterActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreRepository {
    val TAG = "FIREBASE_REPOSITORY"
    var fstore = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    private  val _productList = MutableLiveData<List<Products>>()
    val productList : LiveData<List<Products>>
        get() = _productList

    fun registerAdmin(registerAdmin:RegisterAdmin): Task<Void> {
        val df: DocumentReference = fstore.collection("Sales").document("${user?.uid}")
                .collection("admin").document("Admin Info")
        return df.set(registerAdmin)

    }
    fun addProduct(products: Products): Task<Void>{
        val df: DocumentReference = fstore.collection("Sales").document(user?.uid!!).collection("Products")
                .document(products.productName)
        return df.set(products)
    }




}