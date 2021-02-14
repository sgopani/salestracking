package com.example.salesadmin.collections

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.CollectionItemClickListener
import com.example.salesadmin.LeaveItemClickListener
import com.example.salesadmin.R
import com.example.salesadmin.model.Collections
import com.example.salesadmin.model.Products

class CollectionListAdapter(var collectionList: List<Collections>,var collectionItemClickListeners: CollectionItemClickListener)
    : RecyclerView.Adapter<CollectionListAdapter.CollectionItem>() {

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
        val tvEmployeeName=itemView.findViewById<TextView>(R.id.tv_employee_name_collection)
        val tvcollectionType=itemView.findViewById<TextView>(R.id.tv_collection_type)
        val tvpartyName=itemView.findViewById<TextView>(R.id.tv_party_name_collection)
        val tvamont=itemView.findViewById<TextView>(R.id.tv_amount_collection)
        companion object{
            fun createViewHolder(parent: ViewGroup): CollectionItem {
                val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.collection_list_item, parent, false)
                return CollectionItem (view)
            }
        }
        fun bind(collections: Collections) {

            val employeeName=collections.employeeName
            val collectionsType=collections.collectionType
            val partyName=collections.partyName
            val amount=collections.amount
            tvEmployeeName.text=employeeName
            tvcollectionType.text=collectionsType
            tvpartyName.text=partyName
            tvamont.text=amount.toString()
        }
    }
    fun updateList(list: List<Collections>){
        collectionList=list
        notifyDataSetChanged()
    }
}