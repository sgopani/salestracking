package com.example.salesadmin

import android.content.Context
import android.net.ConnectivityManager

enum class SalesApiStatus { LOADING, ERROR, DONE,EMPTY}

fun isInternetOn(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetwork = cm?.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}