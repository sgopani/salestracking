package com.example.salestracking.databse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class TrackingLocation(
    var employeeName:String="",
    var uid:String="",
    var time:Long=0L,
    var latitude:Double=0.00,
    var longitude:Double=0.00,
    var isCheckedIn:Boolean=true
) : Parcelable {
}