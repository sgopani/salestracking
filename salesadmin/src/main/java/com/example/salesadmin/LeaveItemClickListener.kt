package com.example.salesadmin

import com.example.salesadmin.model.Collections
import com.example.salesadmin.model.Leave


interface LeaveItemClickListener {
    fun onLeaveItemClick(leave: Leave)
}
interface CollectionItemClickListener{
    fun onCollectionItemClick(collection: Collections)
}