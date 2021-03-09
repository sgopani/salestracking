package com.example.salesadmin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
        var products: Products? =null,
        var quantity:Int=0
):Parcelable
