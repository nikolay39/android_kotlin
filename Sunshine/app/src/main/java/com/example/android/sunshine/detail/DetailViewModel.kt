/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.sunshine.detail

import android.app.Application
import androidx.lifecycle.*
import com.example.android.sunshine.R
import com.example.android.sunshine.data.database.ForecastEntry
import com.example.android.sunshine.di.modules.viewmodel.AssistedSavedStateViewModelFactory
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject

/**
 *  The [ViewModel] associated with the [DetailFragment], containing information about the selected
 *  [ForecastEntry].
 */
class DetailViewModel
    @AssistedInject constructor
        (@Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {
    val selectedProperty = savedStateHandle.getLiveData("selectedProperty", null)

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<DetailViewModel> {
        override fun create(savedStateHandle: SavedStateHandle): DetailViewModel  // may be ommited prior kotlin 1.3.60 or after PR #121 in AssistedInject lib
    }


}
//https://proandroiddev.com/saving-ui-state-with-viewmodel-savedstate-and-dagger-f77bcaeb8b08