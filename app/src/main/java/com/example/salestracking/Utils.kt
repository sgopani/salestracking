package com.example.salestracking

import android.content.Context
import android.graphics.Color
import android.net.ConnectivityManager
import com.example.salestracking.repository.FireStoreViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.text.SimpleDateFormat
import java.util.*


var COMPANYUID:String = ""
var requestCode=1
var REQUEST_CODE_LOCATION_PERMISSION=0
var USER=FirebaseAuth.getInstance().currentUser


const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"
const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
const val ACTION_SHOW_TRACKING_FRAGMENT = "ACTION_SHOW_TRACKING_FRAGMENT"

const val LOCATION_UPDATE_INTERVAL = 20000L
const val FASTEST_LOCATION_INTERVAL = 5000L

const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
const val NOTIFICATION_CHANNEL_NAME = "Tracking"
const val NOTIFICATION_ID = 1

const val POLYLINE_COLOR = Color.RED
const val POLYLINE_WIDTH = 8f
const val MAP_ZOOM = 15f


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
const val TIME_FORMAT = "hh:mm:ss aa"
fun toTimeFormat(timeInMillis: Long): String{
    val df = SimpleDateFormat(TIME_FORMAT)
    val date = Date(timeInMillis)
    return df.format(date)
}