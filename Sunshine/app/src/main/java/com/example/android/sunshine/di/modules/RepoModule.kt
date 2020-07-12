package com.example.android.sunshine.di.modules

import com.example.android.sunshine.data.database.ReposDb
import com.example.android.sunshine.data.database.ReposRoom
import com.example.android.sunshine.data.network.ApiService
import com.example.android.sunshine.repository.ForecastRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
@Module(
    includes = [
        DbModule::class,
        ApiModule::class
    ]
)
class RepoModule {
    @Singleton
    @Provides
    fun forecastRepo(db: ReposDb, api: ApiService):ForecastRepository {
        return ForecastRepository(db, api);
    }

}