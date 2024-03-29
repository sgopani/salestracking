package com.example.salesadmin.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salesadmin.SalesApiStatus
import com.example.salesadmin.model.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    private val _productList = MutableLiveData<MutableList<Products>>()
    val productList: LiveData<MutableList<Products>>
        get() = _productList

    private val _employeeList = MutableLiveData<MutableList<Employee>>()
    val employeeList: LiveData<MutableList<Employee>>
        get() = _employeeList

    private val _attendanceList =MutableLiveData<MutableList<Attendance>>()
    val attendanceList: LiveData<MutableList<Attendance>>
        get() = _attendanceList

    private val _partyCollection =MutableLiveData<MutableList<Collections>>()
    val partyCollection: LiveData<MutableList<Collections>>
        get() = _partyCollection

    private val _status = MutableLiveData<SalesApiStatus>()
    val status: LiveData<SalesApiStatus>
        get() = _status

    private val _partyCollectionStatus = MutableLiveData<SalesApiStatus>()
    val partyCollectionStatus: LiveData<SalesApiStatus>
        get() = _partyCollectionStatus

    private val _partiesList = MutableLiveData<MutableList<Party>>()
    val partiesList: LiveData<MutableList<Party>>
        get() = _partiesList

    private val _leaveList = MutableLiveData<List<Leave>>()
    val leaveList: LiveData<List<Leave>>
        get() = _leaveList

    private val _selectedLeave = MutableLiveData<Leave>()
    val selectedLeave :LiveData<Leave>
        get() = _selectedLeave

    private val _selectedCollection = MutableLiveData<Collections>()
    val selectedCollection :LiveData<Collections>
        get() = _selectedCollection

    private val _selectedOrderDetails = MutableLiveData<Order>()
    val selectedOrderDetails:LiveData<Order>
        get() = _selectedOrderDetails

    private val _orderList = MutableLiveData<MutableList<Order>>()
    val orderList : LiveData<MutableList<Order>>
        get() = _orderList

    private val _employeeTrackingList=MutableLiveData<MutableList<TrackingLocation>>()
    val employeeTrackingList:LiveData<MutableList<TrackingLocation>>
        get() = _employeeTrackingList

    fun eventNavigateToLeaveDetail(leave: Leave){
        _selectedLeave.value=leave
    }

    fun eventNavigateToLeaveDetailCompleted(){
        _selectedLeave.value = null
    }

    fun eventNavigateToCollectionDetail(collections: Collections){
        _selectedCollection.value=collections
    }

    fun eventNavigateToCollectionDetailCompleted(){
        _selectedCollection.value = null
    }
    fun eventNavigateToOrderDetail(order: Order){
        _selectedOrderDetails.value=order
    }
    fun eventNavigateToOrderDetailCompleted(){
        _selectedOrderDetails.value = null
    }

    fun eventNavigateToAttendanceCompleted(){
        _attendanceList.value = null
    }
    fun eventNavigateToPartyCollectionCompleted(){
        _partyCollection.value=null
    }


    private val _collectionList = MutableLiveData<List<Collections>>()
    val collectionList: LiveData<List<Collections>>
        get() = _collectionList


    var firebaseRepository = FireStoreRepository()


    fun registerAdminFirebase(registerAdmin: RegisterAdmin) {
        firebaseRepository.registerAdmin(registerAdmin).addOnCompleteListener {
            _status.value = SalesApiStatus.DONE
        }.addOnFailureListener {
            _status.value = SalesApiStatus.ERROR
        }
    }
    fun addParty(party: Party){
        coroutineScope.launch {
            firebaseRepository.addParty(party).addOnCompleteListener {
                _status.value = SalesApiStatus.DONE
            }.addOnFailureListener {
                _status.value = SalesApiStatus.ERROR
            }
        }
    }

    fun addNotification(notification: Notification){
        coroutineScope.launch {
            firebaseRepository.addNotification(notification).addOnCompleteListener {
                _status.value = SalesApiStatus.DONE
            }.addOnFailureListener {
                _status.value = SalesApiStatus.ERROR
            }
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
            val productList = fstore.collection("Sales").document(user!!.uid)
                    .collection("Products")
            productList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Products::class.java)
                        _productList.postValue(innerEvents)
                        Log.d(TAG, "${_productList.value}")
                        _status.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _status.value = SalesApiStatus.ERROR
                }


            }
        }
    }
    fun getEmployeeAttendance(email:String) {
        coroutineScope.launch {
            val productList = fstore.collection("Sales").document("${user?.uid}")
                .collection("employee").document(email).collection("Attendance")
            productList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                   if (querySnapshot?.isEmpty!!) {
                        //_status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                       val innerEvents = querySnapshot?.toObjects(Attendance::class.java)
                       _attendanceList.postValue(innerEvents)
                        //Log.d(TAG, "${_attendanceList.value!!.size}")
                        _status.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _status.value = SalesApiStatus.ERROR
                }


            }
        }
    }
    fun getPartyCollection(name:String) {
        coroutineScope.launch {
            val productList = fstore.collection("Sales").document("${user?.uid}")
                .collection("Collections").whereEqualTo("partyName",name)
            productList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _partyCollectionStatus.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _partyCollectionStatus.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Collections::class.java)
                        _partyCollection.postValue(innerEvents)
                        //Log.d(TAG, "${_attendanceList.value!!.size}")
                        _partyCollectionStatus.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _partyCollectionStatus.value = SalesApiStatus.ERROR
                }


            }
        }
    }

    fun getAllParty() {
        coroutineScope.launch {
            val productList = fstore.collection("Sales")
                .document(user!!.uid).collection("Party")
            productList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Party::class.java)
                        _partiesList.postValue(innerEvents)
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
            val employeeList = fstore.collection("Sales")
                    .document(user!!.uid).collection("employee")
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
    fun getAllLeaves(){
        coroutineScope.launch {
            val leaveList = fstore.collection("Sales")
                    .document(user!!.uid).collection("Leaves")
                    .orderBy("time", Query.Direction.DESCENDING)
            leaveList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Leave::class.java)
                        _leaveList.postValue(innerEvents)
                        _status.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _status.value = SalesApiStatus.ERROR
                }

            }
        }
    }
    fun getAllCollectionsList(){
        coroutineScope.launch {
            val collectionsList = fstore.collection("Sales")
                    .document(user?.uid!!).collection("Collections")
                    .orderBy("time",Query.Direction.DESCENDING)

            collectionsList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG, firebaseFirestoreException?.message.toString())
                    } else {
                        val innerEvents = querySnapshot?.toObjects(Collections::class.java)
                        _collectionList.postValue(innerEvents)
                        _status.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _status.value = SalesApiStatus.ERROR
                }

            }
        }
    }
    fun deleteProducts(name:String) {
        viewModelScope.launch {
            firebaseRepository.deleteProducts(name)
        }
    }
    fun deleteParty(name: String) {
        viewModelScope.launch {
            firebaseRepository.deleteParty(name)
        }
    }

    fun getAllOrderList(){
        coroutineScope.launch {
            val orderList = fstore.collection("Sales")
                .document(user?.uid!!).collection("Order").orderBy("time", Query.Direction.DESCENDING)
            orderList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                    } else {
                         val innerEvents = querySnapshot.toObjects(Order::class.java)
                        _orderList.value=innerEvents
                        _status.value = SalesApiStatus.DONE
                    }

                } catch (t: Throwable) {
                    Log.d(TAG, firebaseFirestoreException?.message.toString())
                    _status.value = SalesApiStatus.ERROR
                }

            }
        }
    }

    fun getEmployeeLocation(){
        coroutineScope.launch {
            val locationList = fstore.collection("Sales")
                .document(user?.uid!!).collection("Tracking").whereEqualTo(
                    "checkedIn",true)
            locationList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        Log.d(TAG,"${_employeeTrackingList.value}")
                    } else {
                        val innerEvents = querySnapshot.toObjects(TrackingLocation::class.java)
                        _employeeTrackingList.value=innerEvents
                        Log.d(TAG,"${_employeeTrackingList.value}")
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
