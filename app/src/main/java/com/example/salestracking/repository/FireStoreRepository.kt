package com.example.salestracking.repository

import android.util.Log
import com.example.salestracking.COMPANYUID
import com.example.salestracking.databse.model.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore


class FireStoreRepository {
    val TAG = "FIREBASE_REPOSITORY"
    var fstore = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser

//    private val _mutableCart = MutableLiveData<MutableList<CartItem>>()
//    val mutableCart : LiveData<MutableList<CartItem>>
//        get() = _mutableCart

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
    fun placeOder(order: Order): Task<Void> {
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
            .collection("Order").document(order.time)
        return df.set(order).addOnSuccessListener {
            //Toast.makeText(c,"",Toast.LENGTH_LONG)
            Log.i(TAG, "Success")
        }.addOnFailureListener {
            Log.i(TAG, "Failure")
        }
    }
    fun addTrackingLocation(trackingLocation: TrackingLocation): Task<Void> {
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
            .collection("Tracking").document(user?.email.toString())
        return df.set(trackingLocation).addOnSuccessListener {
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


    fun markAttendance(attendance: Attendance): Task<Void>{
        val df: DocumentReference = fstore.collection("Sales").document(COMPANYUID)
                .collection("employee")
                .document("${user?.email}").collection("Attendance")
                .document(attendance.date)
        return df.set(attendance).addOnSuccessListener {
            //Toast.makeText(c,"",Toast.LENGTH_LONG)
            Log.i(TAG, "Success")
        }.addOnFailureListener {
            Log.i(TAG, "Failure")
        }
    }


}