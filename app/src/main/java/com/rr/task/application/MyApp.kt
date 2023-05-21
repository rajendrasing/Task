package com.rr.s2sparent.application

import android.app.Application
import android.content.Context
import com.rr.s2sparent.database.roomdatabase.MyAppDatabase

class MyApp : Application() {

    companion object{
        var appContext : Context? = null
        var myRoomDb : MyAppDatabase? = null
    }

    override fun onCreate() {
        super.onCreate()

        appContext = this
        myRoomDb = MyAppDatabase.getDatabase(this)

    }
}

