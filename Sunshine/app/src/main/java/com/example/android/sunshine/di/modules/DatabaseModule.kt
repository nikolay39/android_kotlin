package com.example.android.sunshine.di.modules

import android.content.Context
import androidx.room.Room
import com.example.android.sunshine.data.database.DatabaseManager
import com.example.android.sunshine.model.preference.SunshinePreferences.Companion.context
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Named
import javax.inject.Singleton
@Module
class DatabaseModule {
    @Named("dbName")
    @Provides
    fun provideDbName():String {
        return "sunshine";
    }
    @Singleton
    @Provides
    fun provideRoomDatabase(context: Context, @Named("dbName") dbName: String): DatabaseManager {
        return Room.databaseBuilder(context, DatabaseManager::class.java, dbName).build();
    }
}
