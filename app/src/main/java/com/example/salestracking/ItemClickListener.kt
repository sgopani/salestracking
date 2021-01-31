package com.example.salestracking

import com.example.salestracking.databse.model.Party


interface ItemClickListener {
    fun onLeaveItemClick(party:Party)
}