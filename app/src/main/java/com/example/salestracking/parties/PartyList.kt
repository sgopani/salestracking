package com.example.salestracking.parties

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.ItemClickListener
import com.example.salestracking.R
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.Party
import com.example.salestracking.isInternetOn
import com.example.salestracking.repository.FireStoreViewModel
import java.util.ArrayList

class PartyList : Fragment() {

    private lateinit var rootView: View
    private lateinit var adapter: PartyListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    //private lateinit var progressBar: ProgressBar
    private lateinit var noProduct: TextView
    private var partyList: List<Party> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_party_list)
        //noProduct=rootView.findViewById(R.id.no_product)
        //progressBar = rootView.findViewById(R.id.progress_bar)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        rootView=inflater.inflate(R.layout.party_list, container, false)
        init()
        adapter = PartyListAdapter(partyList,getNewsItemClickListener())
        //progressBar.visibility = View.VISIBLE
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        viewModel = FireStoreViewModel()

        viewModel.getAllParty()
        viewModel.partiesList.observe(this.requireActivity(), Observer { parties ->
            //Log.d("loadData1","${viewModel.productList.value}")
//            noProduct.visibility=View.GONE
            partyList = parties
            //progressBar.visibility=View.GONE
            adapter.partyList = partyList
            adapter.notifyDataSetChanged()
        })
        //
        viewModel.selectedParty.observe(this.viewLifecycleOwner, Observer { partyList->
            if (partyList != null) {
                val action= PartyListDirections.actionPartyListToAddCollection(partyList)
                findNavController().navigate(action)
                viewModel.eventNavigateToPartyListCompleted()
            }
        })

        viewModel.selectedOrderParty.observe(this.viewLifecycleOwner, Observer { partyList->
            if (partyList != null) {
                val action= PartyListDirections.actionPartyListToAddOrders(partyList)
                findNavController().navigate(action)
                viewModel.eventNavigateToPartyListCompleted()
            }
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        //loadData()
        return rootView

    }
    private fun checkInternet(status: SalesApiStatus) {
        when (status) {
            SalesApiStatus.LOADING -> {
                //progressBar.visibility=View.VISIBLE
            }
            SalesApiStatus.ERROR -> {
                if (isInternetOn(this.requireContext())) {
                    Toast.makeText(this.context, "Connected to internet", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.newsList2)
                } else {
                    Toast.makeText(this.context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
                }
                //progressBar.visibility = View.GONE
            }
            SalesApiStatus.DONE -> {
                noProduct.visibility=View.INVISIBLE
                //progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY->{
                //noProduct.text=getString(R.string.no_product)
                noProduct.visibility=View.VISIBLE
                //progressBar.visibility = View.GONE
            }
        }

    }
    private fun getNewsItemClickListener(): ItemClickListener {
        return object : ItemClickListener {

            override fun onPartyItemClick(party: Party) {
                viewModel.eventNavigateToPartyList(party)

            }

            override fun onOrderPartyClick(party: Party) {
                viewModel.eventNavigateToOderPartyList(party)
            }
        }
    }
}