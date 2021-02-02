package com.example.salestracking.collection

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.CollectionItemClickListener
import com.example.salestracking.R
import com.example.salestracking.databse.model.Collections

class CollectionListAdapter(var collectionList: List<Collections>,
                            var collectionItemClickListeners: CollectionItemClickListener): RecyclerView.Adapter<CollectionListAdapter.CollectionItem>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionItem {
        return CollectionItem.createViewHolder(parent)
    }
    private fun getItem(position: Int): Collections {
        return collectionList[position]
    }

    override fun onBindViewHolder(holder: CollectionItem, position: Int) {
        val collections=getItem(position)
        holder.bind(collections)
        holder.itemView.setOnClickListener {
            collectionItemClickListeners.onCollectionItemClick(collections)
        }
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${collectionList.size}")
        return collectionList.size

    }
    class CollectionItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvDate=itemView.findViewById<TextView>(R.id.tv_date_collection)
        val tvcollectionType=itemView.findViewById<TextView>(R.id.tv_collection_type)
        val tvpartyName=itemView.findViewById<TextView>(R.id.tv_party_name_collection)
        val tvamont=itemView.findViewById<TextView>(R.id.tv_amount_received)
        companion object{
            fun createViewHolder(parent: ViewGroup): CollectionItem {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.collection_list_item, parent, false)
                return CollectionItem (view)
            }
        }
        fun bind(collections: Collections) {

            val date=collections.date
            val collectionsType=collections.collectionType
            val partyName=collections.partyName
            val amount=collections.amount
            tvDate.text=date
            tvcollectionType.text=collectionsType
            tvpartyName.text=partyName
            tvamont.text=amount.toString()
        }
    }
}