package com.example.android.sunshine.common

import android.text.format.DateUtils
import com.example.android.sunshine.R
import com.example.android.sunshine.model.preference.SunshinePreferences.Companion.context
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SunshineDateUtils {
    companion object {
        val DAY_IN_MILLIS = TimeUnit.DAYS.toMillis(1)
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
        fun getLocalMidnightFromNormalizedUtcDate(normalizedUtcDate: Long): Long {
            /* The timeZone object will provide us the current user's time zone offset */
            val timeZone: TimeZone  = TimeZone.getDefault();
            /*
             * This offset, in milliseconds, when added to a UTC date time, will produce the local
             * time.
             */
            val gmtOffset = timeZone.getOffset(normalizedUtcDate).toLong()
            val localMidnightMillis:Long = normalizedUtcDate - gmtOffset;
            return localMidnightMillis
        }
        fun elapsedDaysSinceEpoch(utcDate : Long): Long {
            return TimeUnit.MILLISECONDS.toDays(utcDate);
        }
        fun getDayNumber(dateInMillis: Long): Int {
            /*
             * If the date is today, return the localized version of "Today" instead of the actual
             * day name.
             */
            val daysFromEpochToProvidedDate: Long = elapsedDaysSinceEpoch(dateInMillis);
            val daysFromEpochToToday:Long = elapsedDaysSinceEpoch(System.currentTimeMillis());

            val daysAfterToday: Int = (daysFromEpochToProvidedDate - daysFromEpochToToday).toInt()
            return daysAfterToday
        }

    }
}