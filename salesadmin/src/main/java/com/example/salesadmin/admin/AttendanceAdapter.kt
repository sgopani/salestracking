package com.example.salesadmin.admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.example.salesadmin.R
import com.example.salesadmin.model.Attendance
import com.example.salesadmin.toTimeFormat

class AttendanceAdapter (var attendanceList: MutableList<Attendance>)
    : RecyclerView.Adapter<AttendanceAdapter.AttendanceItem>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ):AttendanceItem{
            return AttendanceItem.createViewHolder(parent)
        }

        private fun getItem(position: Int): Attendance {
            return attendanceList[position]
        }

        override fun getItemCount(): Int {
            return attendanceList.size
        }
        override fun onBindViewHolder(holder: AttendanceItem, position: Int) {
            val generator: ColorGenerator = ColorGenerator.MATERIAL
            val color: Int = generator.randomColor
            val attendance: Attendance =getItem(position)
            holder.bind(attendance)
        }
        class AttendanceItem(itemView: View): RecyclerView.ViewHolder(itemView){
            val tvdate=itemView.findViewById<TextView>(R.id.tv_attendance_date)
            val tvtime=itemView.findViewById<TextView>(R.id.tv_attendance_time)
            companion object{
                fun createViewHolder(parent: ViewGroup): AttendanceItem {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.attendance_list_item, parent, false)
                    return AttendanceItem(view)
                }
            }
            fun bind(attendance: Attendance) {
                val time=attendance.time
                val date=attendance.date
                tvtime.text= toTimeFormat(time)
                tvdate.text= date
            }
        }


    }
