package com.example.salestracking.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestracking.COMPANYUID
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.*
import com.example.salestracking.models.Notification
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.HashMap


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

    private val _productList = MutableLiveData<MutableList<Products>>()
    val productList: LiveData<MutableList<Products>>
        get() = _productList

    private val _selectedCollection = MutableLiveData<Collections>()
    val selectedCollection :LiveData<Collections>
        get() = _selectedCollection

    private val _selectedOrderDetails = MutableLiveData<Order>()
    val selectedOrderDetails:LiveData<Order>
        get() = _selectedOrderDetails

    private var _selectedProduct = MutableLiveData<Products>()
    val selectedProduct :LiveData<Products>
        get() = _selectedProduct

    private val _selectedOrderProduct = MutableLiveData<Products>()
    val selectedOrderProduct :LiveData<Products>
        get() = _selectedOrderProduct

    private val _mutableCart = MutableLiveData<MutableList<CartItem>>()
    val mutableCart : LiveData<MutableList<CartItem>>
        get() = _mutableCart

    private val _mutableTotalPrice = MutableLiveData<Double>()
    val mutableTotalPrice : LiveData<Double>
        get() = _mutableTotalPrice

    private val _orderList = MutableLiveData<MutableList<Order>>()
    val orderList : LiveData<MutableList<Order>>
        get() = _orderList

//    private val _mapList = MutableLiveData<MutableMap<String,Any>>()
//    val mapList : LiveData<MutableMap<String,Any>>
//        get() = _mapList



//    fun getCart(): LiveData<MutableList<CartItem>> {
//        if(mutableCart.value==null) {
//            initCart()
//            Log.d("addProductToCart", "${mutableCart.value}")
//            Log.d("addProductToCart", "${_mutableCart.value}")
//        }
//        return mutableCart
//    }

    fun addProductToCart(products: Products):Boolean{
        if(_mutableCart.value==null) {
            initCart()
            //Log.d("addProductToCart", "${mutableCart.value}")
            //Log.d("addProductToCart", "${_mutableCart.value}")
        }

        val cartItemList: MutableList<CartItem>? = _mutableCart.value
        for (cartItem in cartItemList!!) {
            if (cartItem.products!!.productId == products.productId) {
                if (cartItem.quantity == 5) {
                    return false
                }
                val index = cartItemList.indexOf(cartItem)
                cartItem.quantity = cartItem.quantity + 1
                cartItemList[index] = cartItem
                //_mutableCart.postValue(cartItemList)
                _mutableCart.value = cartItemList
                Log.d("addProductToCart", "${_mutableCart.value}")
                Log.d("addProductToCart", "${mutableCart.value}")
                //_mutableCart.value= mutableCart.value
                calculateCartTotal()
                return true
            }
        }
        val cartItem = CartItem(products, 1)
        cartItemList.add(cartItem)

        _mutableCart.value = cartItemList
        calculateCartTotal()
        return true
    }
    fun removeItemFromCart(cartItem: CartItem) {
        if (mutableCart.value == null) {
            return
        }
        val cartItemList: MutableList<CartItem>? =_mutableCart.value
        cartItemList?.remove(cartItem)
        _mutableCart.value = cartItemList
        calculateCartTotal()
    }

    fun changeQuantity(cartItem: CartItem, quantity: Int) {
        if (mutableCart.value == null) {
            return
        }
        val cartItemList: MutableList<CartItem>? = _mutableCart.value
        val index = cartItemList?.indexOf(cartItem)
        Log.d("changeQuantity", "${_mutableCart.value}")
        val updatedItem = CartItem(cartItem.products, quantity)
        Log.d("changeQuantity", "${cartItemList?.indexOf(cartItem)}")

        if (index != null) {
            cartItemList[index] = updatedItem
        }


        _mutableCart.value = cartItemList
        calculateCartTotal()
    }
    private fun calculateCartTotal() {
        if (mutableCart.value == null) {
            return
        }

        var total = 0.0
        val cartItemList: List<CartItem>? = mutableCart.value
        for (cartItem in cartItemList!!) {
            total += cartItem.products?.productPrice!!.toInt() * cartItem.quantity
        }
        _mutableTotalPrice.value = total
    }

    fun getTotalPrice(): LiveData<Double> {
        if (mutableTotalPrice.value == null) {
            _mutableTotalPrice.setValue(0.0)
        }
        return mutableTotalPrice
    }

    private fun initCart() {
        _mutableCart.value=ArrayList()
        _mutableTotalPrice.value=null
        calculateCartTotal()
    }
    fun getEmployeeUid():String{
        return user?.uid.toString()
    }

    fun clear(){
        _mutableCart.value=null
        _mutableTotalPrice.value=null
    }

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

    fun eventNavigateToOderProductList(products: Products){
        _selectedOrderProduct.value=products
    }
    fun eventNavigateToOderProductListCompleted(){
        _selectedOrderProduct.value = null
    }
    fun eventNavigateToOrderDetail(order: Order){
        _selectedOrderDetails.value=order
    }
    fun eventNavigateToOrderDetailCompleted(){
        _selectedOrderDetails.value = null
    }



    fun registerAdminFirebase(employee: Employee){
            firebaseRepository.registerEmployee(employee)
     }
    fun getAllNotification() {
        coroutineScope.launch {
            val notificationList = fstore.collection("Sales")
                .document(COMPANYUID).collection("Notification")
                    .orderBy("time", Query.Direction.DESCENDING)
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
                .document(COMPANYUID).collection("Leaves").whereEqualTo(
                    "userUid",
                    user?.uid.toString()
                )
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
    fun getAllOrderList(){
        coroutineScope.launch {
            val orderList = fstore.collection("Sales")
                .document(COMPANYUID).collection("Order")
            orderList.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                try {
                    _status.value = SalesApiStatus.LOADING
                    if (querySnapshot?.isEmpty!!) {
                        _status.value = SalesApiStatus.EMPTY
                        //Log.d(TAG, firebaseFirestoreException?.message.toString())
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
    fun placeOrder(order: Order){
        coroutineScope.launch {
            firebaseRepository.placeOder(order)
        }
    }
    fun getAllCollectionsList(){
        coroutineScope.launch {
            val collectionsList = fstore.collection("Sales")
                    .document(COMPANYUID).collection("Collections").whereEqualTo(
                    "userUid",
                    user?.uid.toString()
                )
                    .orderBy("time", Query.Direction.DESCENDING)

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
    fun getAllProducts() {
        coroutineScope.launch {
            val productList = fstore.collection("Sales")
                    .document(COMPANYUID).collection("Products")
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
    fun addAttendanceFirebase(attendance: Attendance){
        coroutineScope.launch {
            firebaseRepository.markAttendance(attendance)
        }
    }


//    fun getCart(): LiveData<MutableList<CartItem>> {
//        return firebaseRepository.getCart()
//    }

//    fun removeItemFromCart(cartItem: CartItem?) {
//        return firebaseRepository.removeItemFromCart(cartItem!!)
//    }

//    fun addProductToCart(products: Products): Boolean {
//        return firebaseRepository.addProductToCart(products)
//    }





}