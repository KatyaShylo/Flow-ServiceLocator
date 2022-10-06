package com.example.rmservicelocator.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rmservicelocator.retrofit.Character

@Database(entities = [Character::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun characterDao() : CharacterDao
}