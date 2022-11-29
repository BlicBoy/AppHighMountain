package com.example.highmountain.ui.models

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Percurso::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun percursosDao() : PercursoDao

    companion object{

        @Volatile
        private var INSTANCE : AppDatabase?= null
        fun getDatabase(context: Context) : AppDatabase?{
            if(INSTANCE == null){
                synchronized(AppDatabase::class.java){
                   INSTANCE = Room.databaseBuilder(context,
                       AppDatabase::class.java, "db_percurso").build()
                }
            }
            return INSTANCE
        }
    }
}