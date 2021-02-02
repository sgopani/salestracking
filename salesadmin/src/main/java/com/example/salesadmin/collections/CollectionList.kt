package com.example.salesadmin.collections

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.*
import com.example.salesadmin.leave.LeavesListDirections
import com.example.salesadmin.model.Collections
import com.example.salesadmin.repository.FireStoreViewModel
import java.util.ArrayList

class CollectionList : Fragment() {
    private lateinit var rootView: View
    private var collectionList: List<Collections> = ArrayList()
    private lateinit var adapter: CollectionListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var noCollections:TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_collection_list)
        progressBar = rootView.findViewById(R.id.progress_bar)
        noCollections=rootView.findViewById(R.id.no_collections)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.collection_list, container, false)
        init()
        adapter= CollectionListAdapter(collectionList,getCollectionItemClickListener())
        recyclerView.adapter=adapter
        progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        viewModel = FireStoreViewModel()
        viewModel.getAllCollectionsList()

        viewModel.collectionList.observe(this.requireActivity(), Observer {collections ->
            collectionList=collections
            adapter.collectionList=collections
            adapter.notifyDataSetChanged()
        })
        viewModel.selectedCollection.observe(this.requireActivity(), Observer {collections->
                if (collections != null) {
                    val action= CollectionListDirections.actionCollectionListToCollectionInfo(collections)
                    findNavController().navigate(action)
                    //val navController=this.findNavController()
                    //navController.navigate(NewsListDirections.actionNewsList2ToNewsInfoFragment2(article))
                    //NewsListDirections.actionNewsList2ToNewsInfoFragment2(article)
                    viewModel.eventNavigateToCollectionDetailCompleted()
                }

        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        return rootView
    }
    private fun checkInternet(status: SalesApiStatus) {
        when (status) {
            SalesApiStatus.LOADING -> {
                progressBar.visibility=View.VISIBLE
            }
            SalesApiStatus.ERROR -> {
                if (isInternetOn(this.requireContext())) {
                    Toast.makeText(this.context, "Connected to internet", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this.context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = View.GONE

            }
            SalesApiStatus.DONE -> {
                noCollections.visibility=View.INVISIBLE
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY->{
                noCollections.visibility=View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
    private fun getCollectionItemClickListener(): CollectionItemClickListener {
        return object : CollectionItemClickListener {
            override fun onCollectionItemClick(collection: Collections) {
                viewModel.eventNavigateToCollectionDetail(collection)
            }
        }
    }
}