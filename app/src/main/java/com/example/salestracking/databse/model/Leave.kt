package com.example.salestracking.databse.model

data class Leave(
        var fromdate:String="",
        var toDate:String="",
        var leaveType:String="",
        var reason:String="",
        var time:Long=0L,
        var userUid:String="",
        var status:String="Pending"
) {
}