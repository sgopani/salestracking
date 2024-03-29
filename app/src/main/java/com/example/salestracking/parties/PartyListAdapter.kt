package com.example.salestracking.parties

import android.text.util.Linkify
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salestracking.ItemClickListener
import com.example.salestracking.R
import com.example.salestracking.databse.model.Party
import com.example.salestracking.requestCode
import java.util.*

class PartyListAdapter ( var partyList: List<Party>,var PartyItemClickListeners: ItemClickListener)
    : RecyclerView.Adapter<PartyListAdapter.PartyItem>() {
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
        holder.itemView.setOnClickListener{
            if(requestCode==1){
                PartyItemClickListeners.onPartyItemClick(parties)
            }
            else{
                PartyItemClickListeners.onOrderPartyClick(parties)
            }

        }
        val generator: ColorGenerator = ColorGenerator.MATERIAL
        val color: Int = generator.randomColor
        val drawable = TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                .buildRound(parties.name[0].toString().toUpperCase(Locale.ROOT), color)
        holder.partyImage.setImageDrawable(drawable)
    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${partyList.size}")
        return partyList.size
    }

    class PartyItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val tvName=itemView.findViewById<TextView>(R.id.tv_party_name)
        val tvPhoneNo=itemView.findViewById<TextView>(R.id.tv_party_phoneno)
        val partyImage=itemView.findViewById<ImageView>(R.id.party_imageview)
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
            Linkify.addLinks(tvPhoneNo, Linkify.ALL)
        }
    }
    fun updateList(list: MutableList<Party>){
        partyList=list
        notifyDataSetChanged()
    }
}