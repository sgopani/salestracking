package com.example.salesadmin.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.R
import com.example.salesadmin.model.Attendance
import com.example.salesadmin.model.Collections
import com.example.salesadmin.repository.FireStoreViewModel
import java.util.ArrayList

class PartyCollection : Fragment() {

    private lateinit var rootView: View
    private lateinit var adapter:PartyCollectionAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private var collectionList: MutableList<Collections> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
    }
    fun init(){
        recyclerView = rootView.findViewById(R.id.rv_party_collections)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.party_collection, container, false)
        init()
        adapter = PartyCollectionAdapter(collectionList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)

        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.partyCollection.observe(viewLifecycleOwner, Observer {collection->
            //Toast.makeText(this.requireContext(),"$attendance", Toast.LENGTH_SHORT).show()
            if(collection!=null){
                collectionList=collection
                adapter.collectionList=collectionList
                adapter.notifyDataSetChanged()
                viewModel.eventNavigateToPartyCollectionCompleted()
            }
        })
    }
}