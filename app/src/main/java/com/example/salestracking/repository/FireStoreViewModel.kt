package com.example.salestracking.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestracking.COMPANYUID
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.Collections
import com.example.salestracking.databse.model.Employee
import com.example.salestracking.databse.model.Leave
import com.example.salestracking.databse.model.Party
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

    private val _leaveList = MutableLiveData<MutableList<Leave>>()
    val leaveList: LiveData<MutableList<Leave>>
        get() = _leaveList

    private val _partiesList = MutableLiveData<List<Party>>()
    val partiesList: LiveData<List<Party>>
        get() = _partiesList

    private val _collectionList = MutableLiveData<MutableList<Collections>>()
    val collectionList: LiveData<MutableList<Collections>>
        get() = _collectionList

    private val _selectedParty = MutableLiveData<Party>()
    val selectedParty :LiveData<Party>
        get() = _selectedParty

    private val _selectedOrderParty = MutableLiveData<Party>()
    val selectedOrderParty :LiveData<Party>
        get() = _selectedOrderParty


    private val _selectedCollection = MutableLiveData<Collections>()
    val selectedCollection :LiveData<Collections>
        get() = _selectedCollection

    fun eventNavigateToPartyList(party: Party){
        _selectedParty.value=party
    }
    fun eventNavigateToPartyListCompleted(){
        _selectedParty.value = null
    }
    fun eventNavigateToOderPartyList(party: Party){
        _selectedOrderParty.value=party
    }
    fun eventNavigateToOderPartyListCompleted(){
        _selectedOrderParty.value = null
    }
    fun eventNavigateToCollectionDetail(collections: Collections){
        _selectedCollection.value=collections
    }
    fun eventNavigateToCollectionDetailCompleted(){
        _selectedCollection.value = null
    }
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
    fun applyLeaveFirebase(leave: Leave){
        coroutineScope.launch {
            firebaseRepository.applyLeave(leave)
        }

    }

    fun getAllLeaveList(){
        coroutineScope.launch {
            val leaveList = fstore.collection("Sales")
                .document(COMPANYUID).collection("Leaves").whereEqualTo("userUid", user?.uid.toString())
                .orderBy("time",Query.Direction.DESCENDING)

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
    fun getAllParty() {
        coroutineScope.launch {
            val productList = fstore.collection("Sales")
                .document(COMPANYUID).collection("Party")
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

    fun addCollectionsFirebase(collections: Collections){
        coroutineScope.launch {
            firebaseRepository.addCollection(collections)
        }
    }
    fun getAllCollectionsList(){
        coroutineScope.launch {
            val collectionsList = fstore.collection("Sales")
                    .document(COMPANYUID).collection("Collections").whereEqualTo("userUid", user?.uid.toString())
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
    fun deleteCollection(date: Long) {
        viewModelScope.launch {
            firebaseRepository.deleteCollection(date)
        }
    }
    fun deleteLeaves(date: Long) {
        viewModelScope.launch {
            firebaseRepository.deleteLeave(date)
        }
    }




}