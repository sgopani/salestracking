package com.example.salesadmin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.salesadmin.SalesApiStatus
import com.example.salesadmin.model.Employee
import com.example.salesadmin.model.Products
import com.example.salesadmin.model.RegisterAdmin
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FireStoreViewModel:ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    val TAG = "FIRESTORE_VIEW_MODEL"

    var fstore = FirebaseFirestore.getInstance()
    var user = FirebaseAuth.getInstance().currentUser
    private val _productList = MutableLiveData<List<Products>>()
    val productList: LiveData<List<Products>>
        get() = _productList

    private val _employeeList = MutableLiveData<List<Employee>>()
    val employeeList: LiveData<List<Employee>>
        get() = _employeeList

    private val _status = MutableLiveData<SalesApiStatus>()
    val status: LiveData<SalesApiStatus>
        get() = _status


    var firebaseRepository = FireStoreRepository()
    fun registerAdminFirebase(registerAdmin: RegisterAdmin) {
        firebaseRepository.registerAdmin(registerAdmin).addOnCompleteListener {
            _status.value = SalesApiStatus.DONE
        }.addOnFailureListener {
            _status.value = SalesApiStatus.ERROR
        }
    }

    fun addProductFirebase(products: Products) {
        firebaseRepository.addProduct(products).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _status.value = SalesApiStatus.DONE
            }
            if (task.exception != null) {
                _status.value = SalesApiStatus.ERROR
            }

        }.addOnFailureListener {
            _status.value = SalesApiStatus.ERROR
        }
    }

    init {
        _status.value = SalesApiStatus.LOADING
    }

    fun getAllProducts() {
        coroutineScope.launch {
            val productList = fstore.collection("Sales").document(user!!.uid).collection("Products")
            productList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Products::class.java)
                        _productList.postValue(innerEvents)
                        _status.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _status.value = SalesApiStatus.ERROR
                }


            }
        }
    }

    fun getAllEmployee() {
        coroutineScope.launch {
            val employeeList = fstore.collection("Sales").document(user!!.uid).collection("employee")
            employeeList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Employee::class.java)
                        _employeeList.postValue(innerEvents)
                        _status.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _status.value = SalesApiStatus.ERROR
                }

            }
        }
    }
}