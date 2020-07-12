package com.example.android.sunshine.data.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "forecast")
//TODO weatherId replace new value
@Parcelize
data class ForecastEntry @JvmOverloads constructor (

    var dateTimeMillis: Long = 0,
    var pressure: Double = 0.toDouble(),
    var humidity: Double = 0.toDouble(),
    var windSpeed: Double = 0.toDouble(),
    var windDirection: Double = 0.toDouble(),
    var high: Double = 0.toDouble(),
    var low: Double = 0.toDouble(),
    var weatherId: Int = 0,
    @PrimaryKey @ColumnInfo(name = "entryid")
    var id: String = UUID.randomUUID().toString()
): Parcelable {

}
fun ForecastEntry.getWindMesureemtn() {

}