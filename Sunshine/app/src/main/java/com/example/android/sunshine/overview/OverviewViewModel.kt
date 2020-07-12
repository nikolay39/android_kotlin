/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.sunshine.overview
import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.sunshine.App
import com.example.android.sunshine.common.Event
import com.example.android.sunshine.data.database.ForecastEntry
import com.example.android.sunshine.data.network.Network
import com.example.android.sunshine.data.Result
import com.example.android.sunshine.data.database.getDatabase
import com.example.android.sunshine.data.network.asDatabaseModel
import com.example.android.sunshine.repository.ForecastRepository
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * The [ViewModel] that is attached to the [OverviewFragment].
 */
enum class ApiStatus { LOADING, ERROR, DONE }
class OverviewViewModel@Inject constructor(
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _status = MutableLiveData<ApiStatus>()
    val status: LiveData<ApiStatus>
        get() = _status

    

    // The internal MutableLiveData that stores the status of the most recent request
    //private val _properties = MutableLiveData<List<ForecastEntry>>()

    // The external immutable LiveData for the request status
    val properties: LiveData<List<ForecastEntry>> by lazy {
        try {
            _status.value = ApiStatus.LOADING
            val result  = Result.Success(forecastRepository.forecastEntities)
            _status.value = ApiStatus.DONE
            result.data
        }  catch (e: Exception) {
            _status.value = ApiStatus.ERROR
            val result = Result.Error(e)
            Timber.i("load data error "+ e.message)
            throw IllegalAccessException(e.message)
        }
    }
        //get() = _properties

    // Create a Coroutine scope using a job to be able to cancel when needed
    private var viewModelJob = SupervisorJob()

    /**
     * After the navigation has taken place, make sure navigateToSelectedProperty is set to null
     */
    private val _openDetailForecastEvent = MutableLiveData<Event<ForecastEntry>>()
    val openDetailForecastEvent: LiveData<Event<ForecastEntry>> = _openDetailForecastEvent

    /**
     * Call getMarsRealEstateProperties() on init so we can display status immediately.
     */

    // Refresh Repository
     init {
        //loadForecast()
    }

    /**
     * The Retrofit service returns a
     * coroutine Deferred, which we await to get the result of the transaction.
     */
    //        <!--app:listData="@{viewModel.status}"!-->
    /*
    private fun loadForecast() {
        coroutineScope.launch {
            // Get the Deferred object for our Retrofit request
            try {
                _status.value = ApiStatus.LOADING
                // this will run on a thread managed by Retrofit
                Timber.i("load data from repository: "+ forecastRepository.forecastEntities.value)
                _properties.postValue(forecastRepository.database.selectAll().value)

                Timber.i("load data success")
                _status.value = ApiStatus.DONE
                Timber.i("load data count element " + properties.value)
                Timber.i("load data converted")
            } catch (e: Exception) {
                _status.value = ApiStatus.ERROR
                Timber.i("load data error "+ e.message)
            }
        }
    }
    */
    fun displayPropertyDetails(forecastEntry: ForecastEntry) {
        _openDetailForecastEvent.value = Event(forecastEntry)
    }
    /*
     * When the [ViewModel] is finished, we cancel our coroutine [viewModelJob], which tells the
     * Retrofit service to stop.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
    /*
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(OverviewViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return OverviewViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
    */
}
