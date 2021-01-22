package com.example.salesadmin.model

import com.google.firebase.messaging.RemoteMessage

data class Notification(
     var title:String="",
     var message: String="",
     var time:Long=0L
 )