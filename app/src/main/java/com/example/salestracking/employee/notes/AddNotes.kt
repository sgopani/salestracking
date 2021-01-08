package com.example.salestracking.employee.notes

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.salestracking.R
import com.example.salestracking.databse.SalesDatabase
import com.example.salestracking.databse.dao.NotesDao
import com.example.salestracking.databse.model.Notes
import com.example.salestracking.employee.dashboard.EmployeeDashboardDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AddNotes : Fragment(), View.OnClickListener {
    private lateinit var rootView: View
    private lateinit var noteTitle:EditText
    private lateinit var notesDescription:EditText
    private lateinit var salesDatabase: SalesDatabase
    private lateinit var viewModel: NotesViewModel
    private var notes: Notes? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        salesDatabase= SalesDatabase.getInstance(this.requireContext())
        viewModel=NotesViewModel(salesDatabase)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView=inflater.inflate(R.layout.fragment_add_note, container, false)
        val saveButton=rootView.findViewById<FloatingActionButton>(R.id.save_button)
        noteTitle=rootView.findViewById(R.id.note_title)
        notesDescription=rootView.findViewById(R.id.notes_description)
        saveButton.setOnClickListener (this)
        setHasOptionsMenu(true)
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        arguments.let {
            notes= AddNotesArgs.fromBundle(requireArguments()).Note
            noteTitle.setText(notes?.title)
            notesDescription.setText(notes?.note)
        }
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.save_button -> {
                val title=noteTitle.text.toString().trim()
                val description=notesDescription.text.toString().trim()
                if(title.isEmpty()){
                    noteTitle.error="Note required"
                    noteTitle.requestFocus()
                }
                if (description.isEmpty()){
                    notesDescription.error="Note required"
                    notesDescription.requestFocus()
                    return
                }
                if(notes==null){
                    viewModel.insert(title,description)
                    Toast.makeText(this.requireContext(),"Note Saved",Toast.LENGTH_SHORT).show()
                }
                else{
                    notes!!.title=title
                    notes!!.note=description
                    Log.i("notes data","${notes!!.id}${notes!!.note}${notes!!.title}")
                    viewModel.update(notes!!)
                    Toast.makeText(this.requireContext()," Notes Updated",Toast.LENGTH_SHORT).show()
                }

                val action= AddNotesDirections.actionAddNotesToNotes()
                findNavController().navigate(action)

            }
        }
    }
    fun deleteNote(){
        AlertDialog.Builder(context).apply {
            setTitle("Are you sure?")
            setMessage("You cannot undo this operation")
            setPositiveButton("Yes") { _, _ ->
                viewModel.delete(notes!!)
                val action= AddNotesDirections.actionAddNotesToNotes()
                findNavController().navigate(action)
            }
            setNegativeButton("No") { _, _ ->

            }
        }.create().show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.delete -> {
                if (notes != null) {
                    deleteNote()
                } else {
                    Toast.makeText(this.requireContext(), "Cannot Delete", Toast.LENGTH_SHORT).show()
                }
            }
        }
        return super.onOptionsItemSelected(item)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.delete_menu,menu)
    }
}