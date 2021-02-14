package com.example.salestracking.employee.notes

import android.widget.TextView
import com.example.salestracking.R
import com.example.salestracking.databse.model.Notes
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.databse.model.Leave

class NotesAdapter( var notes: List<Notes>) : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
                LayoutInflater.from(parent.context)
                        .inflate(R.layout.note_layout, parent, false)
        )
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.Note.text = notes[position].note
        holder.title.text = notes[position].title

        holder.view.setOnClickListener {
            val note=notes[position]
            note.id=notes[position].id
            note.title=notes[position].title
            note.note=notes[position].note
            val action = NotesHomeDirections.actionNotesToAddNotes(note)
            Navigation.findNavController(it).navigate(action)
        }
    }

    class NoteViewHolder(val view: View) : RecyclerView.ViewHolder(view){
        var title =view.findViewById<TextView>(R.id.text_view_title)
        var Note=view.findViewById<TextView>(R.id.text_view_note)

    }
    fun updateList(list: List<Notes>){
        notes=list
        notifyDataSetChanged()
    }
}