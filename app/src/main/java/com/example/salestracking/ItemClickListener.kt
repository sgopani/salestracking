package com.example.salestracking

import com.example.salestracking.databse.model.Collections
import com.example.salestracking.databse.model.Party


interface ItemClickListener {
    fun onLeaveItemClick(party:Party)
}
interface CollectionItemClickListener{
    fun onCollectionItemClick(collection: Collections)
}