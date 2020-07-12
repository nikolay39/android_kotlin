package com.example.android.sunshine.data.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

//TODO REPLACE INTO
@Dao
interface ForecastDao {

    /**
     * Observes list of forecast.
     *
     * @return all forecast.
     */
    @Query("SELECT * FROM forecast")
    fun observeforecast(): LiveData<List<ForecastEntry>>
    /**
     * Select a ForecastEntry by id.
     *
     * @param ForecastEntryId the ForecastEntry id.
     * @return the ForecastEntry with ForecastEntryId.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listForecastEntry: List<ForecastEntry>)

    /**
     * Delete all forecast.
     */
    @Query("DELETE FROM forecast")
    suspend fun deleteforecast()

    @Transaction
    suspend fun beginTransaction(listForecastEntry: List<ForecastEntry>) {
        deleteforecast()
        insertAll(listForecastEntry)

    }
}
private lateinit var INSTANCE: DatabaseManager

fun getDatabase(context: Context): DatabaseManager {
    synchronized(DatabaseManager::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                DatabaseManager::class.java,
                "videos").build()
        }
    }
    return INSTANCE
}