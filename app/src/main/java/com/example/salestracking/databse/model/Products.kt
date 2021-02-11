package com.example.salestracking.databse.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class Products(
         var productName:String="",
         var productPrice:String="",
         var productQuantity:String="",
         var productId:String="",
         var time:Long=0L
 ) : Parcelable