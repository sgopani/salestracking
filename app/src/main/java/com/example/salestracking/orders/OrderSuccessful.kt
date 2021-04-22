package com.example.salestracking.orders

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController
import com.example.salestracking.R


class OrderSuccessful : Fragment() {
    private lateinit var rootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.order_successful, container, false)
        val continueTakingOrder:Button= rootView.findViewById(R.id.continue_taking_order_btn)
        continueTakingOrder.setOnClickListener {
            val action=OrderSuccessfulDirections.actionOrderSuccessfulToPartyList()
            findNavController().navigate(action)
        }

        return rootView
    }

}