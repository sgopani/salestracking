package com.example.salesadmin

import android.content.Context
import android.net.ConnectivityManager


enum class SalesApiStatus { LOADING, ERROR, DONE,EMPTY}

fun isInternetOn(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetwork = cm?.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}

object CheckNetClass {
    fun checknetwork(mContext: Context): Boolean {
        val info = (mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager)
                .activeNetworkInfo
        if (info == null || !info.isConnected) {
            return false
        }
        return if (info.isRoaming) {
            // here is the roaming option, you can change it if you want to
            // disable internet while roaming, just return false
            true
        } else true
    }
}