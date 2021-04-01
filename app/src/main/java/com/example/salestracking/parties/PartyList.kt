package com.example.salestracking.parties

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
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
import java.util.*

class PartyList : Fragment() {

    private lateinit var rootView: View
    private lateinit var adapter: PartyListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noParty: TextView
    private var partyList: List<Party> = ArrayList()
    private var searchList: MutableList<Party> = ArrayList()
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_party_list)
        searchEditText=rootView.findViewById(R.id.searchEditText)
        noParty=rootView.findViewById(R.id.no_party)
        progressBar = rootView.findViewById(R.id.progress_bar)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        rootView=inflater.inflate(R.layout.party_list, container, false)
        init()
        adapter = PartyListAdapter(partyList,getNewsItemClickListener())
        progressBar.visibility = View.VISIBLE
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        viewModel = FireStoreViewModel()


        viewModel.getAllParty()
        viewModel.partiesList.observe(this.requireActivity(), Observer { parties ->
            //Log.d("loadData1","${viewModel.productList.value}")
            noParty.visibility=View.GONE
            partyList = parties
            progressBar.visibility=View.GONE
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
                viewModel.eventNavigateToOderPartyListCompleted()
            }

        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                filterList(p0.toString())

            }

        })
        //loadData()
        return rootView

    }
    private fun filterList(filterItem:String){
        searchList.clear()
        for (item in partyList) {
            if (item.name.toLowerCase(Locale.ROOT).contains(filterItem.toLowerCase(Locale.ROOT)))
            {
                searchList.add(item)
                //Log.d("searchList","$searchList")
                adapter.updateList(searchList)
                noParty.visibility=View.INVISIBLE
            }
            else {
                if(searchEditText.text.isEmpty()){
                    if(searchList.isEmpty()){
                        noParty.visibility=View.GONE
                        adapter.updateList(partyList as MutableList<Party>)
                        //loadData()
                    }
                }
            }
        }


        adapter.notifyDataSetChanged()
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
                noParty.visibility=View.INVISIBLE
                //progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY->{
                //noProduct.text=getString(R.string.no_product)
                noParty.visibility=View.VISIBLE
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