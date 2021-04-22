package com.example.salesadmin

import com.example.salesadmin.model.Collections
import com.example.salesadmin.model.Leave
import com.example.salesadmin.model.Order
import com.example.salesadmin.model.TrackingLocation


interface LeaveItemClickListener {
    fun onLeaveItemClick(leave: Leave)
}
interface CollectionItemClickListener{
    fun onCollectionItemClick(collection: Collections)
}
interface OrderDetailsItemClickListener{
    fun onOrderItemClick(order: Order)
}
interface EmployeeAttendanceItemClickListener{
    fun onEmployeeAttendanceClick(email:String)
}
interface PartyItemClickListener{
    fun onPartyClick(name:String)
}