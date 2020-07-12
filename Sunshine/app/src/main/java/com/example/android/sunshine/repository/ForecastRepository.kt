package com.example.android.sunshine.repository

import androidx.lifecycle.LiveData
import com.example.android.sunshine.data.database.DatabaseManager
import com.example.android.sunshine.data.database.ForecastEntry
import com.example.android.sunshine.data.database.ReposDb
import com.example.android.sunshine.data.database.ReposRoom
import com.example.android.sunshine.data.network.ApiService
import com.example.android.sunshine.data.network.Network
import com.example.android.sunshine.data.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class ForecastRepository @Inject constructor(val database: ReposDb, val api:ApiService) {

    val forecastEntities : LiveData<List<ForecastEntry>> = database.selectAll()

    suspend fun refreshForecast() {
        withContext(Dispatchers.IO) {
            val intervalDaysForecast = Network.retrofitService.getForecastWeather("https://andfun-weather.udacity.com/weather/").await()
            //Timber.i("Repository load data count " + intervalDaysForecast.asDatabaseModel().size)

            database.beginTransaction(intervalDaysForecast.asDatabaseModel())
        }
    }
}