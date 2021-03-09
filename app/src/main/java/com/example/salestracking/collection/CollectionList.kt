package com.example.salestracking.collection

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.CollectionItemClickListener
import com.example.salestracking.R
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.Collections
import com.example.salestracking.isInternetOn
import com.example.salestracking.repository.FireStoreViewModel
import java.util.*

class CollectionList : Fragment() {
    private lateinit var rootView: View
    private lateinit var addCollection:Button
    private var collectionList: MutableList<Collections> = ArrayList()
    private lateinit var adapter: CollectionListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private lateinit var noCollections:TextView
    private lateinit var progressBar: ProgressBar
    private var searchList: MutableList<Collections> = ArrayList()
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_collection_list)
        progressBar = rootView.findViewById(R.id.progress_bar)
        noCollections=rootView.findViewById(R.id.no_collections)
        searchEditText=rootView.findViewById(R.id.searchEditText)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.collection_list, container, false)
        init()
        addCollection=rootView.findViewById(R.id.btn_add_collection)
        addCollection.setOnClickListener{
            val action= CollectionListDirections.actionCollectionListToAddCollection()
            findNavController().navigate(action)
        }
        configureCollectionList()
        progressBar.visibility = View.VISIBLE

        viewModel = FireStoreViewModel()
        viewModel.getAllCollectionsList()
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
        viewModel.collectionList.observe(this.requireActivity(), Observer {collections ->
            collectionList=collections
            adapter.collectionList=collections
            adapter.notifyDataSetChanged()
        })

        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkStatus(status)
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
        return rootView
    }

    private fun filterList(filterItem:String){
        searchList.clear()
        for (item in collectionList) {
            if (item.partyName.toLowerCase(Locale.ROOT).contains(filterItem.toLowerCase(Locale.ROOT)))
            {
                searchList.add(item)
                //Log.d("searchList","$searchList")
                adapter.updateList(searchList)
            }
            else {
                if(searchEditText.text.isEmpty()){
                    if(searchList.isEmpty()){
                        adapter.updateList(collectionList)
                        //loadData()
                    }
                }
            }
        }


        adapter.notifyDataSetChanged()
    }
    private fun configureCollectionList(){
        adapter= CollectionListAdapter(collectionList,getCollectionItemClickListener())
        recyclerView.adapter=adapter
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

                    val collectionDate: Long = adapter.getCollectionPosition(position)
                    viewModel.deleteCollection(collectionDate)
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
}