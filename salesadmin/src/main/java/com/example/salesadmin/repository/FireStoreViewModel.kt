package com.example.salesadmin.repository

import androidx.lifecycle.ViewModel
import com.example.salesadmin.model.Products
import com.example.salesadmin.model.RegisterAdmin
import com.example.salesadmin.register.RegisterActivity

class FireStoreViewModel:ViewModel() {
    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = FireStoreRepository()
     fun registerAdminFirebase(registerAdmin: RegisterAdmin){
            firebaseRepository.registerAdmin(registerAdmin)
     }
    fun addProductFirebase(products: Products){
        firebaseRepository.addProduct(products)
    }
}