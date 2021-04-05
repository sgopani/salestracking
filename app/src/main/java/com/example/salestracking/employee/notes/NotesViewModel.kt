package com.example.salestracking.employee.notes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.salestracking.databse.SalesDatabase
import com.example.salestracking.databse.model.Notes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NotesViewModel(database:SalesDatabase):ViewModel() {
    val notesDao = database.notesDao

    private val _notesLiveData = MutableLiveData<Notes>()
    val notesLiveData: LiveData<Notes>
        get() = _notesLiveData

    private val _notesListLiveData = MutableLiveData<MutableList<Notes>>()
    val notesListLiveData: LiveData<MutableList<Notes>>
        get() = _notesListLiveData

    fun insert(title: String, descripton: String) {
        viewModelScope.launch {
            val notes = Notes(title = title, note = descripton)
            _notesLiveData.value = notes
            withContext(Dispatchers.IO) {
                notesDao.insert(notes)
            }
        }
    }

    fun getAllNotes() {
        viewModelScope.launch {
            _notesListLiveData.value = getAll()
        }

    }


    private suspend fun getAll(): MutableList<Notes> {
        return withContext(Dispatchers.IO) {
            val notes: MutableList<Notes> = notesDao.getAllNotes()
            notes
        }

    }

    fun update(notes: Notes) {
        viewModelScope.launch {
            _notesLiveData.value = notes
            withContext(Dispatchers.IO) {
                notesDao.updateNote(notes)
            }

        }
    }

    fun delete(notes: Notes){
        viewModelScope.launch {
            _notesLiveData.value = notes
            withContext(Dispatchers.IO) {
                notesDao.deleteNote(notes)
            }

        }
    }
     fun deleteAll(){
         viewModelScope.launch {
             //_notesLiveData.value = notes
             withContext(Dispatchers.IO) {
                 notesDao.deleteAll()
             }

         }
     }
}