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

//<?xml version="1.0" encoding="utf-8"?>
//<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
//xmlns:app="http://schemas.android.com/apk/res-auto"
//xmlns:tools="http://schemas.android.com/tools"
//android:layout_width="match_parent"
//android:layout_height="match_parent"
//android:background="@drawable/et_custom"
//tools:context=".orders.OrderDetails">
//
//<androidx.recyclerview.widget.RecyclerView
//android:id="@+id/rv_order_details"
//android:layout_width="0dp"
//android:layout_height="0dp"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="8dp"
//android:layout_marginEnd="8dp"
//android:layout_marginRight="8dp"
//app:layout_constraintBottom_toTopOf="@+id/tv_order_detail_total"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintHorizontal_bias="0.0"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/textView35" />
//
//<TextView
//android:id="@+id/tv_party_name_order"
//android:layout_width="0dp"
//android:layout_height="wrap_content"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="16dp"
//android:text="TextView"
//android:textColor="@color/black"
//android:textSize="@dimen/textSizeAll"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toTopOf="parent" />
//
//<TextView
//android:id="@+id/tv_party_address_order"
//android:layout_width="0dp"
//android:layout_height="wrap_content"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="8dp"
//android:layout_marginEnd="8dp"
//android:layout_marginRight="8dp"
//android:text="TextView"
//android:textColor="@color/black"
//android:textSize="@dimen/textSizeAll"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/textView32" />
//
//<TextView
//android:id="@+id/tv_order_party_contact_no"
//android:layout_width="0dp"
//android:layout_height="wrap_content"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="8dp"
//android:layout_marginEnd="8dp"
//android:layout_marginRight="8dp"
//android:text="TextView"
//android:textColor="@color/black"
//android:textSize="@dimen/textSizeAll"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/textView33" />
//
//<TextView
//android:id="@+id/tv_order_details_date"
//android:layout_width="0dp"
//android:layout_height="wrap_content"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="8dp"
//android:layout_marginEnd="8dp"
//android:layout_marginRight="8dp"
//android:text="TextView"
//android:textColor="@color/black"
//android:textSize="@dimen/textSizeAll"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/textView34" />
//
//<!--    <TextView-->
//<!--        android:id="@+id/tv_order_detail_total"-->
//<!--        android:layout_width="wrap_content"-->
//<!--        android:layout_height="wrap_content"-->
//<!--        android:layout_marginEnd="8dp"-->
//<!--        android:layout_marginRight="8dp"-->
//<!--        android:layout_marginBottom="8dp"-->
//<!--        android:text="TextView"-->
//
//<!--        android:textColor="@color/black"-->
//<!--        android:textSize="@dimen/textSizeAll"-->
//<!--        app:layout_constraintBottom_toBottomOf="parent"-->
//<!--        app:layout_constraintEnd_toEndOf="parent" />-->
//
//<TextView
//android:id="@+id/textView32"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="8dp"
//android:text="@string/address"
//android:textColor="@color/black"
//android:textSize="@dimen/textSizeAll"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/tv_party_name_order" />
//
//<TextView
//android:id="@+id/textView33"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="8dp"
//android:text="@string/phone_no"
//android:textColor="@color/black"
//android:textSize="@dimen/textSizeAll"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/tv_party_address_order" />
//
//<TextView
//android:id="@+id/textView34"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_marginStart="8dp"
//android:layout_marginLeft="8dp"
//android:layout_marginTop="8dp"
//android:text="@string/order_date"
//android:textColor="@color/black"
//android:textSize="@dimen/textSizeAll"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/tv_order_party_contact_no" />
//
//<TextView
//android:id="@+id/textView35"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_marginStart="16dp"
//android:layout_marginLeft="16dp"
//android:layout_marginTop="16dp"
//android:text="@string/products"
//android:textColor="@color/background"
//android:textSize="@dimen/text_size"
//app:layout_constraintStart_toStartOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/tv_order_details_date" />
//
//<TextView
//android:id="@+id/textView23"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_marginTop="16dp"
//android:layout_marginEnd="32dp"
//android:layout_marginRight="32dp"
//android:text="@string/Product_quantity"
//android:textColor="@color/background"
//android:textSize="@dimen/text_size"
//app:layout_constraintEnd_toEndOf="parent"
//app:layout_constraintTop_toBottomOf="@+id/tv_order_details_date" />
//</androidx.constraintlayout.widget.ConstraintLayout>