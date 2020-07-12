package com.example.android.sunshine.data.database

import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.*


class ReposRoom(val forecastDao: ForecastDao) : ReposDb{
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.IO)

    override fun selectAll(): LiveData<List<ForecastEntry>> {
       return forecastDao.observeforecast()
    }

    override fun beginTransaction(listForecastEntry: List<ForecastEntry>) {
        coroutineScope.launch {
                forecastDao.beginTransaction(listForecastEntry)
        }
    }
    /*
    override fun deleteAll() {
        forecastDao.deleteforecast()
    }

    override fun insertAll(listForecastEntry: List<ForecastEntry>) {
        forecastDao.insertAll(listForecastEntry)
    }
    */


}
