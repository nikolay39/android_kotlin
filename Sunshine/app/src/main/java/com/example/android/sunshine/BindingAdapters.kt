/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.example.android.sunshine

import android.text.format.DateUtils
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.common.SunshineDateUtils
import com.example.android.sunshine.common.SunshineDateUtils.Companion.getDayNumber
import com.example.android.sunshine.common.SunshineWeatherUtils
import com.example.android.sunshine.data.Result
import com.example.android.sunshine.data.database.ForecastEntry
import com.example.android.sunshine.model.preference.SunshinePreferences
import com.example.android.sunshine.overview.ApiStatus
import com.example.android.sunshine.overview.ForecastLinearAdapter
import java.text.SimpleDateFormat


/**
 * When there is no Mars property data (data is null), hide the [RecyclerView], otherwise show it.
 */

@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<ForecastEntry>?) {
    val adapter = recyclerView.adapter as ForecastLinearAdapter
    adapter.submitList(data)
}
@BindingAdapter("status")
fun bindStatus(statusImageView: ProgressBar, status: ApiStatus?) {
    when (status) {
        ApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
        }
        ApiStatus.ERROR -> {
            statusImageView.visibility = View.GONE
        }
        ApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}
@BindingAdapter("largeIcon")
fun setLargeIcon(iconView: ImageView, wheaterId: Int)
{
    iconView.setImageResource(getLargeArtResourceIdForWeatherCondition(wheaterId))
}
@BindingAdapter("smallIcon")
fun setSmallIcon(iconView: ImageView, wheaterId: Int)
{
    iconView.setImageResource(getSmallArtResourceIdForWeatherCondition(wheaterId))
}
@BindingAdapter("date")
fun setDate(dateView: TextView, longDate: Long) {
    /*
     * NOTE: localDate should be localDateMidnightMillis and should be straight from the
     * database
     *
     * Since we normalized the date when we inserted it into the database, we need to take
     * that normalized date and produce a date (in UTC time) that represents the local time
     * zone at midnight.
     */
    val localDate: Long = SunshineDateUtils.getLocalMidnightFromNormalizedUtcDate(longDate);
    /*
     * In order to determine which day of the week we are creating a date string for, we need
     * to compare the number of days that have passed since the epoch (January 1, 1970 at
     * 00:00 GMT)
     */
    val daysFromEpochToProvidedDate: Long = SunshineDateUtils.elapsedDaysSinceEpoch(localDate);

    /*
     * As a basis for comparison, we use the number of days that have passed from the epoch
     * until today.
     */
    val daysFromEpochToToday: Long = SunshineDateUtils.elapsedDaysSinceEpoch(System.currentTimeMillis());
    // show full date || false
    /*
     * If the date we're building the String for is today's date, the format
     * is "Today, June 24"
     */

    val readableDate =  DateUtils.formatDateTime(dateView.context, localDate,
        DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_NO_YEAR or DateUtils.FORMAT_SHOW_WEEKDAY)
    //show full date
    if (daysFromEpochToProvidedDate   <  daysFromEpochToToday + 7) {
        val dayNumber: Int = getDayNumber(localDate);
        val dayName : String
        when (dayNumber) {
            0 -> dayName = dateView.context.getString(com.example.android.sunshine.R.string.today);
            1 -> dayName = dateView.context.getString(com.example.android.sunshine.R.string.tomorrow);
            else -> dayName = SimpleDateFormat("EEEE").format(localDate);
        }
        if(daysFromEpochToProvidedDate == daysFromEpochToToday) {
            /*
             * Since there is no localized format that returns "Today" or "Tomorrow" in the API
             * levels we have to support, we take the name of the day (from SimpleDateFormat)
             * and use it to replace the date from DateUtils. This isn't guaranteed to work,
             * but our testing so far has been conclusively positive.
             *
             * For information on a simpler API to use (on API > 18), please check out the
             * documentation on DateFormat#getBestDateTimePattern(Locale, String)
             * https://developer.android.com/reference/android/text/format/DateFormat.html#getBestDateTimePattern
             */
            val localizedDayName: String = SimpleDateFormat("EEEE").format(localDate);
            dateView.setText(readableDate.replace(localizedDayName, dayName))
        } else if(daysFromEpochToProvidedDate < daysFromEpochToToday +1) {
            dateView.setText(readableDate);
        } else {
            /* If the input date is less than a week in the future, just return the day name. */
            dateView.setText(dayName);
        }
    } else {
        dateView.setText(DateUtils.formatDateTime(dateView.context, localDate,
            DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_ABBREV_ALL or DateUtils.FORMAT_SHOW_WEEKDAY))
    }
}
@BindingAdapter("description", "format")
fun setStringForWeatherCondition(description: TextView, weatherId: Int, format: String) {
    val stringId: Int
    if (weatherId >= 200 && weatherId <= 232) {
        stringId = com.example.android.sunshine.R.string.condition_2xx;
    } else if (weatherId >= 300 && weatherId <= 321) {
        stringId = com.example.android.sunshine.R.string.condition_3xx;
    } else when (weatherId) {
        500 -> stringId = com.example.android.sunshine.R.string.condition_500;
        501 -> stringId = com.example.android.sunshine.R.string.condition_501;
        502 -> stringId = com.example.android.sunshine.R.string.condition_502;
        503 -> stringId = com.example.android.sunshine.R.string.condition_503;
        504 -> stringId = com.example.android.sunshine.R.string.condition_504;
        511 -> stringId = com.example.android.sunshine.R.string.condition_511;
        520 -> stringId = com.example.android.sunshine.R.string.condition_520;
        531 -> stringId = com.example.android.sunshine.R.string.condition_531;
        600 -> stringId = com.example.android.sunshine.R.string.condition_600;
        601 -> stringId = com.example.android.sunshine.R.string.condition_601;
        602 -> stringId = com.example.android.sunshine.R.string.condition_602;
        611 -> stringId = com.example.android.sunshine.R.string.condition_611;
        612 -> stringId = com.example.android.sunshine.R.string.condition_612;
        615 -> stringId = com.example.android.sunshine.R.string.condition_615;
        616 -> stringId = com.example.android.sunshine.R.string.condition_616;
        620 -> stringId = com.example.android.sunshine.R.string.condition_620;
        621 -> stringId = com.example.android.sunshine.R.string.condition_621;
        622 -> stringId = com.example.android.sunshine.R.string.condition_622;
        701 -> stringId = com.example.android.sunshine.R.string.condition_701;
        711 -> stringId = com.example.android.sunshine.R.string.condition_711;
        721 -> stringId = com.example.android.sunshine.R.string.condition_721;
        731 -> stringId = com.example.android.sunshine.R.string.condition_731;
        741 -> stringId = com.example.android.sunshine.R.string.condition_741;
        751 -> stringId = com.example.android.sunshine.R.string.condition_751;
        761 -> stringId = com.example.android.sunshine.R.string.condition_761;
        762 -> stringId = com.example.android.sunshine.R.string.condition_762;
        771 -> stringId = com.example.android.sunshine.R.string.condition_771;
        781 -> stringId = com.example.android.sunshine.R.string.condition_781;
        800 -> stringId = com.example.android.sunshine.R.string.condition_800;
        801 -> stringId = com.example.android.sunshine.R.string.condition_801;
        802 -> stringId = com.example.android.sunshine.R.string.condition_802;
        803 -> stringId = com.example.android.sunshine.R.string.condition_803;
        804 -> stringId = com.example.android.sunshine.R.string.condition_804;
        900 -> stringId = com.example.android.sunshine.R.string.condition_900;
        901 -> stringId = com.example.android.sunshine.R.string.condition_901;
        902 -> stringId = com.example.android.sunshine.R.string.condition_902;
        903 -> stringId = com.example.android.sunshine.R.string.condition_903;
        904 -> stringId = com.example.android.sunshine.R.string.condition_904;
        905 -> stringId = com.example.android.sunshine.R.string.condition_905;
        906 -> stringId = com.example.android.sunshine.R.string.condition_906;
        951 -> stringId = com.example.android.sunshine.R.string.condition_951;
        952 -> stringId = com.example.android.sunshine.R.string.condition_952;
        953 -> stringId = com.example.android.sunshine.R.string.condition_953;
        954 -> stringId = com.example.android.sunshine.R.string.condition_954;
        955 -> stringId = com.example.android.sunshine.R.string.condition_955;
        956 -> stringId = com.example.android.sunshine.R.string.condition_956;
        957 -> stringId = com.example.android.sunshine.R.string.condition_957;
        958 -> stringId = com.example.android.sunshine.R.string.condition_958;
        959 -> stringId = com.example.android.sunshine.R.string.condition_959;
        960 -> stringId = com.example.android.sunshine.R.string.condition_960;
        961 -> stringId = com.example.android.sunshine.R.string.condition_961;
        962 -> stringId = com.example.android.sunshine.R.string.condition_962;
        else -> stringId = com.example.android.sunshine.R.string.condition_unknown
    }
    description.setText(String.format(format,  description.context.getString(stringId)))
    description.contentDescription = String.format(format, description.context.getString(stringId))

}


@BindingAdapter("highTemp", "format")
fun highTemp(highView: TextView, highTemp: Double, format : String) {
    var temperature: Double = highTemp
    if (!SunshinePreferences.isMetric()) {
        temperature = SunshineWeatherUtils.celsiusToFahrenheit(temperature)
        //Timber.d("imperial");
    }
    highView.setText(String.format(format, temperature))
    highView.contentDescription = String.format(format, temperature)
}
@BindingAdapter("lowTemp", "format")
fun lowTemp(lowView: TextView, lowTemp: Double, format: String) {
    var temperature: Double = lowTemp
    if (!SunshinePreferences.isMetric()) {
        temperature = SunshineWeatherUtils.celsiusToFahrenheit(temperature)
        //Timber.d("imperial");
    }
    lowView.setText(String.format(format, temperature))
    lowView.contentDescription = String.format(format, temperature)
}

@BindingAdapter("humidity", "format")
fun setHumidity(humidityView: TextView, humidityValue: Double,  format: String) {
    humidityView.setText(String.format(format, humidityValue))
    humidityView.contentDescription = String.format(format, humidityValue)
}
@BindingAdapter("pressure", "format")
fun setpressure(pressureView: TextView, pressureValue: Double,  format: String) {
    pressureView.setText(String.format(format, pressureValue))
    pressureView.contentDescription = String.format(format, pressureValue)
}
fun getSmallArtResourceIdForWeatherCondition(weatherId: Int): Int {

    /*
     * Based on weather code data for Open Weather Map.
     */
    if (weatherId >= 200 && weatherId <= 232) {
        return com.example.android.sunshine.R.drawable.ic_storm;
    } else if (weatherId >= 300 && weatherId <= 321) {
        return com.example.android.sunshine.R.drawable.ic_light_rain;
    } else if (weatherId >= 500 && weatherId <= 504) {
        return com.example.android.sunshine.R.drawable.ic_rain;
    } else if (weatherId == 511) {
        return com.example.android.sunshine.R.drawable.ic_snow;
    } else if (weatherId >= 520 && weatherId <= 531) {
        return com.example.android.sunshine.R.drawable.ic_rain;
    } else if (weatherId >= 600 && weatherId <= 622) {
        return com.example.android.sunshine.R.drawable.ic_snow;
    } else if (weatherId >= 701 && weatherId <= 761) {
        return com.example.android.sunshine.R.drawable.ic_fog;
    } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
        return com.example.android.sunshine.R.drawable.ic_storm;
    } else if (weatherId == 800) {
        return com.example.android.sunshine.R.drawable.ic_clear;
    } else if (weatherId == 801) {
        return com.example.android.sunshine.R.drawable.ic_light_clouds;
    } else if (weatherId >= 802 && weatherId <= 804) {
        return com.example.android.sunshine.R.drawable.ic_cloudy;
    } else if (weatherId >= 900 && weatherId <= 906) {
        return com.example.android.sunshine.R.drawable.ic_storm;
    } else if (weatherId >= 958 && weatherId <= 962) {
        return com.example.android.sunshine.R.drawable.ic_storm;
    } else if (weatherId >= 951 && weatherId <= 957) {
        return com.example.android.sunshine.R.drawable.ic_clear;
    }
    return com.example.android.sunshine.R.drawable.ic_storm;
}
@BindingAdapter("windSpeed", "windDirection")
fun setWindMeasurement(windView: TextView, windSpeed: Double, windDirection : Double) {
    var windFormat: Int = com.example.android.sunshine.R.string.format_wind_kmh
    var _windSpeed: Double = windSpeed

    if (!SunshinePreferences.isMetric()) {
        windFormat = com.example.android.sunshine.R.string.format_wind_mph
        _windSpeed = .621371192237334f * windSpeed
    }
    val windString: String = SunshineWeatherUtils.getWindDirection(windDirection)
    windView.setText(String.format(windView.context.getString(windFormat), _windSpeed, windString))

}
 /**
 * Helper method to provide the art resource ID according to the weather condition ID returned
 * by the OpenWeatherMap call. This method is very similar to
 *
 *   {@link #getSmallArtResourceIdForWeatherCondition(int)}.
 *
 * The difference between these two methods is that this method provides larger assets, used
 * in the "today view" of the list, as well as in the DetailActivity.
 *
 * @param weatherId from OpenWeatherMap API response
 *                  See http://openweathermap.org/weather-conditions for a list of all IDs
 *
 * @return resource ID for the corresponding icon. -1 if no relation is found.
 */
fun getLargeArtResourceIdForWeatherCondition(weatherId: Int): Int {

    /*
     * Based on weather code data for Open Weather Map.
     */
    if (weatherId >= 200 && weatherId <= 232) {
        return com.example.android.sunshine.R.drawable.art_storm;
    } else if (weatherId >= 300 && weatherId <= 321) {
        return com.example.android.sunshine.R.drawable.art_light_rain;
    } else if (weatherId >= 500 && weatherId <= 504) {
        return com.example.android.sunshine.R.drawable.art_rain;
    } else if (weatherId == 511) {
        return com.example.android.sunshine.R.drawable.art_snow;
    } else if (weatherId >= 520 && weatherId <= 531) {
        return com.example.android.sunshine.R.drawable.art_rain;
    } else if (weatherId >= 600 && weatherId <= 622) {
        return com.example.android.sunshine.R.drawable.art_snow;
    } else if (weatherId >= 701 && weatherId <= 761) {
        return com.example.android.sunshine.R.drawable.art_fog;
    } else if (weatherId == 761 || weatherId == 771 || weatherId == 781) {
        return com.example.android.sunshine.R.drawable.art_storm;
    } else if (weatherId == 800) {
        return com.example.android.sunshine.R.drawable.art_clear;
    } else if (weatherId == 801) {
        return com.example.android.sunshine.R.drawable.art_light_clouds;
    } else if (weatherId >= 802 && weatherId <= 804) {
        return com.example.android.sunshine.R.drawable.art_clouds;
    } else if (weatherId >= 900 && weatherId <= 906) {
        return com.example.android.sunshine.R.drawable.art_storm;
    } else if (weatherId >= 958 && weatherId <= 962) {
        return com.example.android.sunshine.R.drawable.art_storm;
    } else if (weatherId >= 951 && weatherId <= 957) {
        return com.example.android.sunshine.R.drawable.art_clear;
    }
    return com.example.android.sunshine.R.drawable.art_storm;
}

