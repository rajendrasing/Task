package com.rr.s2sparent.database.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.rr.s2sparent.database.roomdatabase.dao.VideoDao
import com.rr.task.model.VideoStatus

@Database(entities = [VideoStatus::class], version = 1)
abstract class MyAppDatabase :RoomDatabase(){

    abstract fun videoDao() : VideoDao

    companion object{
        @Volatile
        private var INSTANCE : MyAppDatabase? = null

        fun getDatabase(context : Context):MyAppDatabase{
            if (INSTANCE==null){
                synchronized(this){
                    INSTANCE = Room.databaseBuilder(context,
                        MyAppDatabase::class.java,
                        "MyAppDatabase")
                        .build()
                }
            }
            return INSTANCE!!
        }

    }
}