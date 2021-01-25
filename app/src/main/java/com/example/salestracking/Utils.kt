package com.example.salestracking

import android.content.Context
import android.net.ConnectivityManager
import java.text.SimpleDateFormat
import java.util.*


var COMPANYUID:String = ""
enum class SalesApiStatus { LOADING, ERROR, DONE,EMPTY}
fun isInternetOn(context: Context): Boolean {
    val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
    val activeNetwork = cm?.activeNetworkInfo
    return activeNetwork != null && activeNetwork.isConnected
}
const val SIMPLE_DATE_FORMAT = "dd MMM yyyy"
fun toSimpleDateFormat(timeInMillis: Long): String {
    val df = SimpleDateFormat(SIMPLE_DATE_FORMAT)
    val date = Date(timeInMillis)
    return df.format(date)
}