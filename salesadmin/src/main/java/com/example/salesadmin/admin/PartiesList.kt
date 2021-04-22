package com.example.salesadmin.admin

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salesadmin.*
import com.example.salesadmin.model.Order
import com.example.salesadmin.model.Party
import com.example.salesadmin.repository.FireStoreViewModel
import java.util.*

class PartiesList : Fragment() {
    private lateinit var rootView: View
    private lateinit var addPartyBtn: Button
    private lateinit var adapter: PartiesListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noProduct: TextView
    private var partyList: MutableList<Party> = ArrayList()
    private var searchList: MutableList<Party> = ArrayList()
    private lateinit var searchEditText: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this.requireActivity()).get(FireStoreViewModel::class.java)
    }
    private fun init() {
        addPartyBtn = rootView.findViewById(R.id.add_parties_btn)
        recyclerView = rootView.findViewById(R.id.rv_partiesList)
        noProduct=rootView.findViewById(R.id.no_party)
        progressBar = rootView.findViewById(R.id.progress_bar)
        searchEditText=rootView.findViewById(R.id.searchEditText)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        rootView=inflater.inflate(R.layout.parties_list, container, false)
        init()
        configurePartyList()
        progressBar.visibility = View.VISIBLE
        viewModel.getAllParty()
        viewModel.partiesList.observe(this.requireActivity(), Observer { parties ->
            //Log.d("loadData1","${viewModel.productList.value}")
            noProduct.visibility=View.GONE
            partyList = parties
            progressBar.visibility=View.GONE
            adapter.partyList = partyList
            adapter.notifyDataSetChanged()
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkStatus(status)
        })
        viewModel.partyCollectionStatus.observe(this.requireActivity(), Observer {status ->
            when(status){
                SalesApiStatus.LOADING-> {

                }
                SalesApiStatus.EMPTY->{
                    Toast.makeText(this.context,"No collections taken from this party",Toast.LENGTH_LONG).show()
                }
                SalesApiStatus.DONE->{

                }
                SalesApiStatus.ERROR->{
                    Toast.makeText(this.context,"Something went wrong",Toast.LENGTH_LONG).show()
                }
            }

        })
        addPartyBtn.setOnClickListener {
            val action = PartiesListDirections.actionPartiesListToAddParties()
            findNavController().navigate(action)
        }
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
                     noProduct.visibility=View.INVISIBLE
                 }
                 else {
                     if(searchEditText.text.isEmpty()){
                         if(searchList.isEmpty()){
                             noProduct.visibility=View.GONE
                             adapter.updateList(partyList)
                             //loadData()
                         }
                     }
                 }
             }



         adapter.notifyDataSetChanged()
         }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.partyCollection.observe(viewLifecycleOwner, Observer {collection->
            if(collection!=null){

                val action=PartiesListDirections.actionPartiesListToPartyCollection()
                findNavController().navigate(action)
            }
        })

    }

    private fun configurePartyList(){
        adapter = PartiesListAdapter(partyList,getPartyItemClickListener())
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    private fun checkStatus(status: SalesApiStatus) {
        when (status) {
            SalesApiStatus.LOADING -> {
                progressBar.visibility=View.VISIBLE
            }
            SalesApiStatus.ERROR -> {
                if (isInternetOn(this.requireContext())) {
                    Toast.makeText(this.context, "Connected to internet", Toast.LENGTH_SHORT).show()
                    //findNavController().navigate(R.id.newsList2)
                } else {
                    Toast.makeText(this.context, "Please Check Your Internet Connection", Toast.LENGTH_SHORT).show()
                }
                progressBar.visibility = View.GONE

            }
            SalesApiStatus.DONE -> {
                noProduct.visibility=View.INVISIBLE
                progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY->{
                //noProduct.text=getString(R.string.no_product)
                noProduct.visibility=View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }
    }
    private var itemTouchHelper: ItemTouchHelper = ItemTouchHelper(object :
            ItemTouchHelper.SimpleCallback(
                    0,
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
        private val background = ColorDrawable(Color.RED)
        override fun onMove(
                recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
            val position = viewHolder.layoutPosition
            AlertDialog.Builder(context).apply {
                setTitle("Are you sure you want to delete?")
                setMessage("You cannot undo this operation")
                setPositiveButton("Yes") { _, _ ->

                    val collectionName=adapter.getPartyPosition(position)
                    viewModel.deleteParty(collectionName)
                    adapter.remove(position)
                }
                setNegativeButton("No") { _, _ ->
                    adapter.notifyItemChanged(position)
                }
            }.create().show()


        }

        override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            val itemView: View = viewHolder.itemView
            val backgroundCornerOffset = -20
            when {
                dX > 0 -> { // Swiping to the right
                    background.setBounds(
                            itemView.left, itemView.top,
                            itemView.left + dX.toInt() + backgroundCornerOffset,
                            itemView.bottom
                    )
                }
                dX < 0 -> { // Swiping to the left
                    background.setBounds(
                            itemView.right + dX.toInt() - backgroundCornerOffset,
                            itemView.top, itemView.right, itemView.bottom
                    )
                }
                else -> { // view is unSwiped
                    background.setBounds(0, 0, 0, 0)
                }
            }
            background.draw(c)
        }
    })
    private fun getPartyItemClickListener(): PartyItemClickListener {
        return object : PartyItemClickListener {

            override fun onPartyClick(name: String) {
                viewModel.getPartyCollection(name)
            }

        }
    }
}