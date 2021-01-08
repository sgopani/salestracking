package com.example.salestracking.databse

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.salestracking.databse.dao.NotesDao
import com.example.salestracking.databse.model.Notes

@Database(entities = [Notes::class],version = 1,exportSchema = false)
abstract class SalesDatabase: RoomDatabase(){
    abstract val notesDao:NotesDao
    companion object{

        @Volatile
        private var INSTANCE: SalesDatabase? = null

        fun getInstance(context: Context): SalesDatabase{
            synchronized(this){
                var instance = INSTANCE

                if (instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            SalesDatabase::class.java,
                            "pebble_user_database"
                    ).fallbackToDestructiveMigration()
                            .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}