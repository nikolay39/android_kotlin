package com.example.android.sunshine.di.modules.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
/*
* @see com.squareup.inject.assisted.dagger2.AssistedModule
* @see com.squareup.inject.assisted.AssistedInject.Factory
*/
interface AssistedSavedStateViewModelFactory<T : ViewModel> {
    fun create(savedStateHandle: SavedStateHandle): T
}