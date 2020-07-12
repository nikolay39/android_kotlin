package com.example.android.sunshine.data.network

import android.util.Log
import com.example.android.sunshine.common.SunshineDateUtils
import com.example.android.sunshine.data.database.ForecastEntry
import com.squareup.moshi.Json
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

data class IntervalDaysForecast(val city: City, val message: Float,
                           @Json(name = "list")
                           val dayForecasts: List<Dayforecast>) {
}
data class Dayforecast(val dt: Double,
                       val temp: Temp,
                       val weather: List<Wheater>,
                       val pressure: Double,
                       val humidity: Double,
                       val speed: Double,
                       val deg: Int,
                       val clouds: Int) {}

data class Wheater(val id: Int, val main: String, val description: String, val icon: String) {}

data class Temp(val day: Double,
           val min: Double,
           val max: Double,
           val night: Double,
           val eve: Double,
           val morn: Double) {}


data class City(val id: Int, val name: String, val coord: Coord) {}
data class Coord(val lon: Double, val lat: Double) {}

fun IntervalDaysForecast.asDatabaseModel(): List<ForecastEntry> {
    Timber.i("StartProcessing")
    val listForecastEntry = ArrayList<ForecastEntry>()
    val listDaysForecast = this.dayForecasts
    //val city: City = this.city

    //val cityLatitude: Double = city.coord.lat
    //val cityLongitude: Double = city.coord.lon

    val normalizedUtcStartDay: Long = SunshineDateUtils.getNormalizedUtcDateForToday();
    listDaysForecast.forEachIndexed { index, dayforecast ->

        val dateTimeMillis = normalizedUtcStartDay + SunshineDateUtils.DAY_IN_MILLIS * index;
        val pressure = dayforecast.pressure
        val humidity: Double = dayforecast.humidity
        val windSpeed: Double = dayforecast.speed
        val windDirection = dayforecast.deg.toDouble()

        /*
         * Description is in a child array called "weather", which is 1 element long.
         * That element also contains a weather code.
         */
        val listWeather: List<Wheater> = dayforecast.weather
        val weather = listWeather.get(0)
        val weatherId = weather.id


        /*
         * Temperatures are sent by Open Weather Map in a child object called "temp".
         *
         * Editor's Note: Try not to name variables "temp" when working with temperature.
         * It confuses everybody. Temp could easily mean any number of things, including
         * temperature, temporary variable, temporary folder, temporary employee, or many
         * others, and is just a bad variable name.
         */
        val temp: Temp = dayforecast.temp
        val high = temp.max
        val low = temp.min;

        val forecastEntry: ForecastEntry = ForecastEntry(
            dateTimeMillis,
            pressure,
            humidity,
            windSpeed,
            windDirection,
            high,
            low,
            weatherId);
        listForecastEntry.add(forecastEntry);
    }
    Log.i("Transfer object", "size listForecastEntry"+listForecastEntry.size)

    return listForecastEntry
}
