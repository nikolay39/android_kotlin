package com.example.android.sunshine.data.database

import androidx.lifecycle.LiveData

interface ReposDb {
        fun selectAll(): LiveData<List<ForecastEntry>>
        fun beginTransaction(listForecastEntry: List<ForecastEntry>)
        //fun insertAll(listForecastEntry: List<ForecastEntry>)
        //fun deleteAll()

}