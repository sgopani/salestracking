package com.example.salestracking.repository

import android.util.Log
import com.example.salestracking.COMPANYUID
import com.example.salestracking.databse.model.Employee
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FireStoreRepository {
    val TAG = "FIREBASE_REPOSITORY"
    var fstore = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

    fun registerEmployee(employee: Employee): Task<Void>
    {
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
            .collection("employee").document("${user?.email}")
        return df.set(employee).addOnSuccessListener {
            Log.i(TAG,"Success")
        }.addOnFailureListener {
            Log.i(TAG,"Failure")
        }

    }
}