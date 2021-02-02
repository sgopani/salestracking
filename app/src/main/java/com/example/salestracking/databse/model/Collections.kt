package com.example.salestracking.databse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Collections(
    var partyName:String="",
    var amount: Long=0L,
    var date:String="",
    var collectionNote:String="",
    var collectionType:String="",
    var employeeName:String="",
    var userUid:String="",
    var time:Long=0L
) : Parcelable