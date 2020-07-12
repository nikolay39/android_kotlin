package com.example.android.sunshine.common

import com.example.android.sunshine.model.preference.SunshinePreferences
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit

class SunshineWeatherUtils{
    companion object  {
        //val context : WeakReference<Context> = WeakReference(App.applicationContext())
        fun  getNormalizedUtcDateForToday():Long {
            /*
             * This number represents the number of milliseconds that have elapsed since January
             * 1st, 1970 at midnight in the GMT time zone.
             */
            val utcNowMillis: Long = System.currentTimeMillis();
            /*
             * This TimeZone represents the device's current time zone. It provides us with a means
             * of acquiring the offset for local time from a UTC time stamp.
             */
            val currentTimeZone: TimeZone = TimeZone.getDefault();
            /*
             * The getOffset method returns the number of milliseconds to add to UTC time to get the
             * elapsed time since the epoch for our current time zone. We pass the current UTC time
             * into this method so it can determine changes to account for daylight savings time.
             */
            val gmtOffsetMillis = currentTimeZone.getOffset(utcNowMillis).toLong();
            /*
             * UTC time is measured in milliseconds from January 1, 1970 at midnight from the GMT
             * time zone. Depending on your time zone, the time since January 1, 1970 at midnight (GMT)
             * will be greater or smaller. This variable represents the number of milliseconds since
             * January 1, 1970 (GMT) time.
             */
           val timeSinceEpochLocalTimeMillis = utcNowMillis + gmtOffsetMillis;

            /* This method simply converts milliseconds to days, disregarding any fractional days */
            val  daysSinceEpochLocal = TimeUnit.MILLISECONDS.toDays(timeSinceEpochLocalTimeMillis);

            /*
             * Finally, we convert back to milliseconds. This time stamp represents today's date at
             * midnight in GMT time. We will need to account for local time zone offsets when
             * extracting this information from the database.
             */
            val normalizedUtcMidnightMillis = TimeUnit.DAYS.toMillis(daysSinceEpochLocal);

            return normalizedUtcMidnightMillis;
        }
        fun celsiusToFahrenheit(temperatureInCelsius: Double): Double {
            val temperatureInFahrenheit:Double = (temperatureInCelsius * 1.8) + 32;
            return temperatureInFahrenheit;
        }
        fun getWindDirection(degrees: Double): String {
            var direction: String = "Unknown"
            if (degrees >= 337.5 || degrees < 22.5) {
                direction = "N";
            } else if (degrees >= 22.5 && degrees < 67.5) {
                direction = "NE";
            } else if (degrees >= 67.5 && degrees < 112.5) {
                direction = "E";
            } else if (degrees >= 112.5 && degrees < 157.5) {
                direction = "SE";
            } else if (degrees >= 157.5 && degrees < 202.5) {
                direction = "S";
            } else if (degrees >= 202.5 && degrees < 247.5) {
                direction = "SW";
            } else if (degrees >= 247.5 && degrees < 292.5) {
                direction = "W";
            } else if (degrees >= 292.5 && degrees < 337.5) {
                direction = "NW";
            }
            return direction
        }

    }
}