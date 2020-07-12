package com.example.android.sunshine.work

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.android.sunshine.data.database.DatabaseManager
import com.example.android.sunshine.data.database.getDatabase
import com.example.android.sunshine.model.preference.SunshinePreferences
import com.example.android.sunshine.receiver.ForecastReceiver
import com.example.android.sunshine.repository.ForecastRepository
import retrofit2.HttpException
import javax.inject.Inject

private val REQUEST_CODE = 0

class RefreshDataWorker   @Inject constructor(val repository: ForecastRepository,  appContext : Context, params: WorkerParameters):
    CoroutineWorker(appContext, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }
    private val notifyIntent = Intent(appContext, ForecastReceiver::class.java)
    override suspend fun doWork(): Payload {

        return try {
            repository.refreshForecast()
            val notificationsEnabled:Boolean = SunshinePreferences.areNotificationsEnabled()
            if(notificationsEnabled) {
                PendingIntent.getBroadcast(
                    applicationContext,
                    REQUEST_CODE,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
            }
            Payload(Result.SUCCESS)
        } catch(e: HttpException) {
            Payload(Result.RETRY)
        }
    }

}
