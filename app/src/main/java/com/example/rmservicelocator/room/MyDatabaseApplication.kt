package com.example.rmservicelocator.room

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.rmservicelocator.service.ServiceLocator.init

class MyDatabaseApplication : Application() {

    var appDatabase: AppDatabase? = null

    override fun onCreate() {
        super.onCreate()

        init(this)
        appDatabase = Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "characterDatabase"
        ).build()
    }
}

val Context.appDatabase: AppDatabase
    get() = when (this) {
        is MyDatabaseApplication -> requireNotNull(appDatabase)
        else -> applicationContext.appDatabase
    }