package com.example.android.sunshine.di.modules

import com.example.android.sunshine.data.database.DatabaseManager
import com.example.android.sunshine.data.database.ForecastDao
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Singleton

@Module
class DbDao {
    @Provides
    @Singleton
    fun provideDao(database: DatabaseManager): ForecastDao {
        return database.ForecastDao()
    }
}
