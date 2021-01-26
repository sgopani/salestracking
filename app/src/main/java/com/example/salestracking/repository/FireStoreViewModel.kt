package com.example.salestracking.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.salestracking.COMPANYUID
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.models.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FireStoreViewModel:ViewModel() {
    val TAG = "FIRESTORE_VIEW_MODEL"
    var firebaseRepository = FireStoreRepository()
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)
    var fstore = FirebaseFirestore.getInstance()
    var user=FirebaseAuth.getInstance().currentUser
    private val _status = MutableLiveData<SalesApiStatus>()
    val status: LiveData<SalesApiStatus>
        get() = _status

    private val _notificationList = MutableLiveData<List<Notification>>()
    val notificationList: LiveData<List<Notification>>
        get() = _notificationList

    fun registerAdminFirebase(employee: Employee){
            firebaseRepository.registerEmployee(employee)
     }
    fun getAllNotification() {
        coroutineScope.launch {
            val notificationList = fstore.collection("Sales")
                .document(COMPANYUID).collection("Notification")
                    .orderBy("time",Query.Direction.DESCENDING)
            notificationList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Notification::class.java)
                        _notificationList.postValue(innerEvents)
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