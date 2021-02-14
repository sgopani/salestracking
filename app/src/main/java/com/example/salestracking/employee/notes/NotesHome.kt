package com.example.salestracking.employee.notes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.salestracking.R
import com.example.salestracking.databse.SalesDatabase
import com.example.salestracking.databse.model.Notes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class NotesHome : Fragment(), View.OnClickListener {
    private lateinit var salesDatabase: SalesDatabase
    private lateinit var viewModel: NotesViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var rootView:View
    private  var notesList: List<Notes> =ArrayList()
    private var searchList: MutableList<Notes> = ArrayList()
    private lateinit var searchEditText: EditText
    private lateinit var adapter:NotesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView= inflater.inflate(R.layout.fragment_notes, container, false)
        salesDatabase= SalesDatabase.getInstance(this.requireContext())
        viewModel = NotesViewModel(salesDatabase)
        viewModel.getAllNotes()
        recyclerView=rootView.findViewById(R.id.recyclerview_notes)
        searchEditText=rootView.findViewById(R.id.searchEditText)
        recyclerView.setHasFixedSize(true)
        adapter= NotesAdapter(notesList)
        recyclerView.adapter=adapter
        recyclerView.layoutManager=LinearLayoutManager(this.requireContext())
        //recyclerView.layoutManager=StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL)
        viewModel.notesListLiveData.observe(this.viewLifecycleOwner, Observer {notes->
            notesList=notes
            adapter.notes=notes
            adapter.notifyDataSetChanged()

        })
        // Inflate the layout for this fragment
        val addNotes=rootView.findViewById<FloatingActionButton>(R.id.floating_button_add)
        addNotes.setOnClickListener(this)
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0.toString())

            }
        })
        return rootView
    }
    private fun filterList(filterItem:String){
        searchList.clear()
        for (item in notesList) {
            if (item.title.toLowerCase(Locale.ROOT).contains(filterItem.toLowerCase(Locale.ROOT)))
            {
                searchList.add(item)
                //Log.d("searchList","$searchList")
                adapter.updateList(searchList)
                //noProduct.visibility=View.INVISIBLE
            }
            else {
                if(searchEditText.text.isEmpty()){
                    if(searchList.isEmpty()){
                        //noProduct.visibility=View.GONE
                        adapter.updateList(notesList)
                        //loadData()
                    }
                }
            }
        }


        adapter.notifyDataSetChanged()
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