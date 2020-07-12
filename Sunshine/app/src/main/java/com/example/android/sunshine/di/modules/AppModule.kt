package com.example.android.sunshine.di.modules

import android.content.Context
import com.example.android.sunshine.App
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {

    @Singleton
    @Provides
    fun app(): App {
        return app;
    }
    @Singleton
    @Provides
    fun  getAppContext(): Context {
        return App.applicationContext();
    }
}
