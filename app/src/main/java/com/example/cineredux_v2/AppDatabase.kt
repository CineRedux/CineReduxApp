package com.example.cineredux_v2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CachedMovie::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}