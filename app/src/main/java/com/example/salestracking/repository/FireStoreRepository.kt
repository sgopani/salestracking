package com.example.salestracking.repository

import android.util.Log
import com.example.salestracking.COMPANYUID
import com.example.salestracking.databse.model.Employee
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

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
    fun getUserInfo(): Task<DocumentSnapshot> {
        val userList=fstore.collection("Sales")
                    .document("OgpCRNT7mSg17vA5eCTEDjK6svk1").collection("employee")
                    .document(user!!.email.toString())
        return userList.get()
    }



}