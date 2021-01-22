package com.example.salestracking.notification

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.R
import com.example.salestracking.models.Notification

class NotificationAdapter( var notificationList: List<Notification>): RecyclerView.Adapter<NotificationAdapter.NotificationItem>() {
    //    private var productList= mutableListOf<Products>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationItem {
        return NotificationItem.createViewHolder(parent)
    }
    private fun getItem(position: Int):Notification {
        return notificationList[position]
    }

    override fun onBindViewHolder(holder: NotificationItem, position: Int) {
        val notification=getItem(position)
        holder.bind(notification)
    }
//    fun submitList(list:MutableList<Products>){
//        productList = list
//        notifyDataSetChanged()
//    }

    override fun getItemCount(): Int {
        Log.d("getItemCount","${notificationList.size}")
        return notificationList.size

    }
    class NotificationItem(itemView: View): RecyclerView.ViewHolder(itemView){
        val title=itemView.findViewById<TextView>(R.id.tv_notifications_title)
        val message=itemView.findViewById<TextView>(R.id.tv_notification_message)
        companion object{
            fun createViewHolder(parent: ViewGroup): NotificationItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.notification_list_item, parent, false)
                return NotificationItem (view)
            }
        }
        fun bind(notification: Notification) {
            val notificationTitle=notification.title
            val notificationMessage=notification.message
            title.text=notificationTitle
            message.text=notificationMessage

        }
    }
}