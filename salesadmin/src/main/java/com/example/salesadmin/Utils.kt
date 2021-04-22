package com.example.salesadmin

import android.content.Context
import android.net.ConnectivityManager
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


enum class SalesApiStatus { LOADING, ERROR, DONE,EMPTY}
const val TIME_FORMAT = "hh:mm:ss aa"

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

const val SIMPLE_DATE_FORMAT = "dd MMM yyyy"
fun toSimpleDateFormat(timeInMillis: Long): String {
    val df = SimpleDateFormat(SIMPLE_DATE_FORMAT)
    val date = Date(timeInMillis)
    return df.format(date)
}
fun toTimeFormat(timeInMillis: Long): String{
    val df = SimpleDateFormat(TIME_FORMAT)
    val date = Date(timeInMillis)
    return df.format(date)
}