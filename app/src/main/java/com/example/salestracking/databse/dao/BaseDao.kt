package com.example.salestracking.databse.dao

import androidx.room.Insert

interface BaseDao<T> {

    @Insert
    fun insert(obj: T)

}