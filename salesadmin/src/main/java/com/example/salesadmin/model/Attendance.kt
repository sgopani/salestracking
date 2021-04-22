package com.example.salesadmin.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Attendance (
        var checkInTime:String="",
        var date:String="",
        var time:Long=0L,
        var uid:String=""
) : Parcelable