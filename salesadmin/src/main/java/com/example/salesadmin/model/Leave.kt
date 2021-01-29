package com.example.salesadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Leave(
        var fromdate:String="",
        var toDate:String="",
        var leaveType:String="",
        var reason:String="",
        var time:Long=0L,
        var userUid:String="",
        var status:String="Pending",
        var name:String=""
): Parcelable {
}