package com.example.android.sunshine.di.modules

import com.example.android.sunshine.data.network.ApiService
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class ApiModule {
    @Singleton
    @Provides
    fun api(retrofit: Retrofit):ApiService {
        return retrofit.create(ApiService::class.java)
    }
    @Named("endpoint")
    @Provides
    fun endpoint():String {
        return "https://andfun-weather.udacity.com/weather/";
    }

    @Provides
    fun retrofit(@Named("endpoint") baseUrl:String, client: OkHttpClient,
                 gsonConverterFactory : MoshiConverterFactory,
                 addCallAdapterFactory: CoroutineCallAdapterFactory): Retrofit {
        return Retrofit.Builder()
            .baseUrl(endpoint())
            .client(client)
            .addCallAdapterFactory(addCallAdapterFactory)
            .addConverterFactory(gsonConverterFactory)
            .build();
    }
    @Provides
    fun gsonConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)

    }
    @Provides
    fun moshi():Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

    }

    @Provides
    fun addCallAdapterFactory():CoroutineCallAdapterFactory {
        return CoroutineCallAdapterFactory()

    }
    @Provides
    fun httploggingInterceptor(): HttpLoggingInterceptor {
        val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor

    }
    @Provides
    fun okHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            //.addNetworkInterceptor(new StethoInterceptor())
            .build();
    }
}
