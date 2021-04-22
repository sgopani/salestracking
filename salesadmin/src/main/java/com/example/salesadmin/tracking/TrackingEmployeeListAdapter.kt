package com.example.salesadmin.tracking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salesadmin.R
import com.example.salesadmin.model.TrackingLocation
import com.example.salesadmin.toTimeFormat
import java.util.*

class TrackingEmployeeListAdapter(var trackingEmployeeList: MutableList<TrackingLocation>):
    RecyclerView.Adapter<TrackingEmployeeListAdapter.TrackingEmployeeItem>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TrackingEmployeeItem {
        return TrackingEmployeeItem.createViewHolder(parent)
    }

    private fun getItem(position: Int): TrackingLocation {
        return trackingEmployeeList[position]
    }

    override fun getItemCount(): Int {
        return trackingEmployeeList.size
    }

    override fun onBindViewHolder(holder: TrackingEmployeeItem, position: Int) {
        val generator: ColorGenerator = ColorGenerator.MATERIAL
        val color: Int = generator.randomColor
        val trackingLocation: TrackingLocation = getItem(position)
        holder.bind(trackingLocation)
        val drawable =
            TextDrawable.builder().beginConfig().withBorder(4).endConfig()
                .buildRound(
                    trackingLocation.employeeName[0].toString().toUpperCase(Locale.ROOT),
                    color
                )
        holder.employeeImage.setImageDrawable(drawable)
    }

    class TrackingEmployeeItem(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEmployeeName = itemView.findViewById<TextView>(R.id.tv_tracking_employee_list_name)
        val tvlastLocationTime=itemView.findViewById<TextView>(R.id.tv_employee_last_location_time)
        val employeeImage = itemView.findViewById<ImageView>(R.id.employeeImage)

        companion object {
            fun createViewHolder(parent: ViewGroup): TrackingEmployeeItem {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.tracking_employee_list_item, parent, false)
                return TrackingEmployeeItem(view)
            }
        }

        fun bind(trackingLocation: TrackingLocation) {
            val employeeName = trackingLocation.employeeName
            val lastLocationTime= toTimeFormat(trackingLocation.time)
            tvEmployeeName.text=employeeName
            tvlastLocationTime.text=lastLocationTime


        }
    }

}