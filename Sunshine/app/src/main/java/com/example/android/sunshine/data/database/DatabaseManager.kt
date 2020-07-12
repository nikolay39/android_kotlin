package com.example.android.sunshine.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ForecastEntry::class], version = 1, exportSchema = false)
abstract class DatabaseManager : RoomDatabase() {
    abstract fun ForecastDao(): ForecastDao
}