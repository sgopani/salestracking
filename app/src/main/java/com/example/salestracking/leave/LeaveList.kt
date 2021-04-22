package com.example.salestracking.leave

import android.app.AlertDialog
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.salestracking.R
import com.example.salestracking.SalesApiStatus
import com.example.salestracking.databse.model.Leave
import com.example.salestracking.isInternetOn
import com.example.salestracking.repository.FireStoreViewModel
import java.util.*

class LeaveList : Fragment() {
    private lateinit var rootView:View
    private lateinit var applyLeaveBtn:Button
    private var leaveList: MutableList<Leave> = ArrayList()
    private lateinit var adapter: LeaveListAdapter
    private lateinit var viewModel: FireStoreViewModel
    private lateinit var recyclerView: RecyclerView
    private var searchList: MutableList<Leave> = ArrayList()
    private lateinit var searchEditText: EditText
    //private lateinit var progressBar: ProgressBar
    private lateinit var noLeaves:TextView
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
    }
    private fun init() {
        recyclerView = rootView.findViewById(R.id.rv_leave_list)
        //progressBar = rootView.findViewById(R.id.progress_bar)
        noLeaves=rootView.findViewById(R.id.no_leaves)
        searchEditText=rootView.findViewById(R.id.searchEditText)
        searchEditText.setHint(R.string.search_leave)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.leave_list, container, false)
        init()
        configureLeaveList()
        viewModel = FireStoreViewModel()
        viewModel.getAllLeaveList()
        viewModel.leaveList.observe(this.requireActivity(), Observer { leaves ->
            leaveList = leaves
            adapter.LeaveList = leaveList
            adapter.notifyDataSetChanged()
            leaveList.forEach { leave ->
                Log.d("leave", leave.reason)
            }
        })
        viewModel.status.observe(this.requireActivity(), Observer { status ->
            checkInternet(status)
        })
        applyLeaveBtn=rootView.findViewById(R.id.apply_leave_btn)
        applyLeaveBtn.setOnClickListener{
            val action=LeaveListDirections.actionLeaveListToApplyLeave()
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
        return rootView
    }
    private fun filterList(filterItem:String){
        searchList.clear()
        for (item in leaveList) {
            if (item.reason.toLowerCase(Locale.ROOT).contains(filterItem.toLowerCase(Locale.ROOT)))
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
                        adapter.updateList(leaveList)
                        //loadData()
                    }
                }
            }
        }


        adapter.notifyDataSetChanged()
    }
    private fun configureLeaveList(){
        adapter= LeaveListAdapter(leaveList)
        recyclerView.adapter=adapter
        //progressBar.visibility = View.VISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
        recyclerView.setHasFixedSize(true)
        itemTouchHelper.attachToRecyclerView(recyclerView)
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
                    Toast.makeText(
                        this.context,
                        "Please Check Your Internet Connection",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                //progressBar.visibility = View.GONE
            }
            SalesApiStatus.DONE -> {
                noLeaves.visibility = View.INVISIBLE
                //progressBar.visibility = View.GONE
            }
            SalesApiStatus.EMPTY -> {
                noLeaves.visibility = View.VISIBLE
                //progressBar.visibility = View.GONE
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
                    val leaveDate: Long = adapter.getCollectionPosition(position)
                    viewModel.deleteLeaves(leaveDate)
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