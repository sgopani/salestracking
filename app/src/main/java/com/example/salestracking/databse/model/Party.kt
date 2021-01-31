package com.example.salestracking.databse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Party (
    var name: String="",
    var address:String="",
    var phoneNo:String="",
    var contactName:String="",
    var uid:String="",
    var time:Long=0L
) : Parcelable {

}