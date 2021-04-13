package com.example.salesadmin.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salesadmin.R
import com.example.salesadmin.model.Collections


class PartyCollectionAdapter (var collectionList: MutableList<Collections>)
    : RecyclerView.Adapter<PartyCollectionAdapter.CollectionItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectionItem {
        return CollectionItem.createViewHolder(parent)
    }

    private fun getItem(position: Int): Collections{
        return collectionList[position]
    }

    override fun getItemCount(): Int {
        return collectionList.size
    }

    override fun onBindViewHolder(holder: CollectionItem, position: Int) {
        val generator: ColorGenerator = ColorGenerator.MATERIAL
        val color: Int = generator.randomColor
        val collections: Collections= getItem(position)
        holder.bind(collections)
    }

    class CollectionItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvdate = itemView.findViewById<TextView>(R.id.tv_party_collection_date)
        val tvamount = itemView.findViewById<TextView>(R.id.tv_party_collection_amount)
        val tvEmpName = itemView.findViewById<TextView>(R.id.tv_party_collection_employee_name)
        companion object {
            fun createViewHolder(parent: ViewGroup): CollectionItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.party_collection_list_item, parent, false)
                return CollectionItem(view)
            }
        }

        fun bind(collections: Collections) {
           val amount = collections.amount
            val date = collections.date
            val empName=collections.employeeName
            tvdate.text = date
            tvamount.text=amount.toString()
            tvEmpName.text=empName

//            tvdate.text = date
        }
    }
}