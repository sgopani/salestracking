package com.example.salestracking.databse.dao

import androidx.room.*
import com.example.salestracking.databse.model.Notes

@Dao
interface NotesDao:BaseDao<Notes> {
    @Insert
    fun addNote(note:Notes)

    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAllNotes():MutableList<Notes>

    @Update
    fun updateNote(note:Notes)

    @Delete
    fun deleteNote(note:Notes)


}