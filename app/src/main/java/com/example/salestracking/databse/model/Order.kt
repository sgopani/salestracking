package com.example.salestracking.databse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.HashMap

@Parcelize
data class Order(
    var party: Party? =null,
    //var ProductList: HashMap<String,ArrayList<CartItem>> = hashMapOf(),
    //var productlist: HashMap<String, ArrayList<CartItem>> = arrayListOf<>(),
    var productlist:MutableList<CartItem> = arrayListOf(),
    var total:String="",
    var time:String="",
    var employeename:String="",
    var employeeUid:String=""

) : Parcelable {
}