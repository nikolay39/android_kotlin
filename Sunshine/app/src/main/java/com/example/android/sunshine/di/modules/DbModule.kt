package com.example.android.sunshine.di.modules

import com.example.android.sunshine.data.database.ForecastDao
import com.example.android.sunshine.data.database.ReposDb
import com.example.android.sunshine.data.database.ReposRoom
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

@Module(includes = [DatabaseModule::class, DbDao::class])
class DbModule {
    @Provides
    @Singleton
    fun reposDb(forecastDao: ForecastDao): ReposDb {
        return ReposRoom(forecastDao)
    }
}
