package com.example.salestracking

import com.example.salestracking.databse.model.Collections
import com.example.salestracking.databse.model.Party


interface ItemClickListener {
    fun onPartyItemClick(party:Party)
    fun onOrderPartyClick(party:Party)

}
interface CollectionItemClickListener{
    fun onCollectionItemClick(collection: Collections)
}