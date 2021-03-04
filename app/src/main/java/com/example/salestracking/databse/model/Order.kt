package com.example.salestracking.databse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order (
    var party: Party,
    var Productlist: List<CartItem>,
    var total:String="",
    var time:String="",
    var employeename:String="",
    var employeeUid:String=""

) : Parcelable {
}