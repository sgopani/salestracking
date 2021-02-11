package com.example.salestracking.databse.model


import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Attendance (
        var uid:String="",
        var time:Long=0L,
        var date:String="",
        var checkInTime:String="",
) : Parcelable