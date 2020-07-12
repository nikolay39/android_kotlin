package com.example.android.sunshine.di.modules.viewmodel

import androidx.lifecycle.ViewModel
import com.example.android.sunshine.detail.DetailViewModel
import com.example.android.sunshine.overview.OverviewViewModel
import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@AssistedModule
@Module(includes = [AssistedInject_MyUiBuilderModule::class])
abstract class MyUiBuilderModule {
    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel::class)
    abstract fun bindsMainViewModel(vm: OverviewViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    abstract fun bindsOtherViewModel(f: DetailViewModel.Factory): AssistedSavedStateViewModelFactory<out ViewModel>
}