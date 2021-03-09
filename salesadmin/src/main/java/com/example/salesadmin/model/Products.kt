package com.example.salesadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
 data class Products(
         var productName:String="",
         var productPrice:String="",
         var productDescription:String="",
         var productId:String="",
         var time:Long=0L
 ) : Parcelable