package com.example.salesadmin.admin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.R
import com.example.salesadmin.model.Party

class PartiesListAdapter( var partyList: MutableList<Party>): RecyclerView.Adapter<PartiesListAdapter.PartyItem>() {
    //    private var productList= mutableListOf<Products>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartyItem {
        return PartyItem.createViewHolder(parent)
    }
    private fun getItem(position: Int):Party {
        return partyList[position]
    }

    override fun onBindViewHolder(holder: PartyItem, position: Int) {
        val parties=getItem(position)
        holder.bind(parties)
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${partyList.size}")
        return partyList.size

    }
    fun getPartyPosition(Position:Int):String{
        return partyList[Position].name
    }
    fun remove(position: Int){
        partyList.removeAt(position)
        notifyItemRemoved(position)
    }
    class PartyItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvName=itemView.findViewById<TextView>(R.id.tv_party_name)
        val tvPhoneNo=itemView.findViewById<TextView>(R.id.tv_party_phoneno)
        val tvAddress=itemView.findViewById<TextView>(R.id.tv_part_address)
        companion object{
            fun createViewHolder(parent: ViewGroup): PartyItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.parties_list_item, parent, false)
                return PartyItem (view)
            }
        }
        fun bind(party: Party) {
            val partyName=party.name
            val phone_no=party.phoneNo
            val address=party.address
            tvName.text=partyName
            tvPhoneNo.text=phone_no
            tvAddress.text=address

        }
    }
}