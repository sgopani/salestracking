package com.example.salesadmin

import com.example.salesadmin.model.Collections
import com.example.salesadmin.model.Leave
import com.example.salesadmin.model.Order


interface LeaveItemClickListener {
    fun onLeaveItemClick(leave: Leave)
}
interface CollectionItemClickListener{
    fun onCollectionItemClick(collection: Collections)
}
interface OrderDetailsItemClickListener{
    fun onOrderItemClick(order: Order)
}