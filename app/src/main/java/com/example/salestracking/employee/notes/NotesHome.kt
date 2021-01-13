package com.example.salestracking.employee.notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.salestracking.R
import com.example.salestracking.databse.SalesDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton

class NotesHome : Fragment(), View.OnClickListener {
    private lateinit var salesDatabase: SalesDatabase
    private lateinit var viewModel: NotesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var rootView:View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView= inflater.inflate(R.layout.fragment_notes, container, false)
        salesDatabase= SalesDatabase.getInstance(this.requireContext())
        viewModel = NotesViewModel(salesDatabase)
        viewModel.getAllNotes()
        recyclerView=rootView.findViewById(R.id.recyclerview_notes)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager=LinearLayoutManager(this.requireContext())
        //recyclerView.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        viewModel.notesListLiveData.observe(this.viewLifecycleOwner, Observer {notes->
            recyclerView.adapter=NotesAdapter(notes)
        })
        // Inflate the layout for this fragment
        val addNotes=rootView.findViewById<FloatingActionButton>(R.id.floating_button_add)
        addNotes.setOnClickListener(this)

        return rootView
    }
    override fun onClick(view: View?) {
        when(view?.id){
            R.id.floating_button_add -> {
                val action=NotesHomeDirections.actionNotesToAddNotes()
                findNavController().navigate(action)
            }
        }
    }

}