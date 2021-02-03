package com.example.salestracking.repository

import android.util.Log
import android.widget.Toast
import com.example.salestracking.COMPANYUID
import com.example.salestracking.databse.model.Collections
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.databse.model.Leave
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

    fun registerEmployee(employee: Employee): Task<Void> {
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
                .collection("employee").document("${user?.email}")
        return df.set(employee).addOnSuccessListener {
            Log.i(TAG, "Success")
        }.addOnFailureListener {
            Log.i(TAG, "Failure")
        }

    }

    fun getUserInfo(): Task<DocumentSnapshot> {
        val userList = fstore.collection("Sales")
                .document(COMPANYUID).collection("employee")
                .document(user!!.email.toString())
        return userList.get()
    }

    fun applyLeave(leave: Leave): Task<Void> {
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
                .collection("Leaves").document(leave.time.toString())
        return df.set(leave).addOnSuccessListener {
            Log.i(TAG, "Success")
        }.addOnFailureListener {
            Log.i(TAG, "Failure")
        }

    }

    fun addCollection(collection: Collections): Task<Void> {
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
                .collection("Collections").document(collection.time.toString())
        return df.set(collection).addOnSuccessListener {
            //Toast.makeText(c,"",Toast.LENGTH_LONG)
            Log.i(TAG, "Success")
        }.addOnFailureListener {
            Log.i(TAG, "Failure")
        }
    }

    fun deleteCollection(date: Long) {
        val df: Task<Void> = fstore.collection("Sales").document(COMPANYUID)
                .collection("Collections").document(date.toString()).delete().addOnSuccessListener {
                    Log.i(TAG, "Success")
                }.addOnFailureListener {
                    Log.i(TAG, "Failure")
                }
    }

    fun deleteLeave(date: Long) {
        val df: Task<Void> = fstore.collection("Sales").document(COMPANYUID)
                .collection("Leaves").document(date.toString()).delete().addOnSuccessListener {
                    Log.i(TAG, "Success")
                }.addOnFailureListener {
                    Log.i(TAG, "Failure")
                }


    }
}