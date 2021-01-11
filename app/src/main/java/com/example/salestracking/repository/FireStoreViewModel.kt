package com.example.salestracking.repository

import androidx.lifecycle.ViewModel
import com.example.salestracking.databse.model.Employee

class FireStoreViewModel:ViewModel() {
    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = FireStoreRepository()

    fun registerAdminFirebase(employee: Employee){
            firebaseRepository.registerEmployee(employee)
     }
}