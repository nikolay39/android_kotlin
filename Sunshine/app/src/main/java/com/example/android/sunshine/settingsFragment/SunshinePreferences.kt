/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine.model.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.sunshine.App;
import com.example.android.sunshine.R;
import java.lang.ref.WeakReference

const val PREF_COORD_LAT: String = "coord_lat"
const val PREF_COORD_LONG: String = "coord_long"

class SunshinePreferences {

    companion object {
        val context : WeakReference<Context> = WeakReference(App.applicationContext())
        fun resetLocationCoordinates() {
            val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.get())
            val editor: SharedPreferences.Editor = sp.edit()

            editor.remove(PREF_COORD_LAT);
            editor.remove(PREF_COORD_LONG);
            editor.apply();
        }
        fun isMetric(): Boolean {
            val sp: SharedPreferences  = PreferenceManager.getDefaultSharedPreferences(context.get());

            // get key
            val keyForUnits: String = (context.get())!!.getString(R.string.pref_units_key);
            //defaultUnits
            val defaultUnits: String = (context.get())!!.getString(R.string.pref_units_metric);
            //prefferedUnits
            val preferredUnits: String? = sp.getString(keyForUnits, defaultUnits);
            //
            val metric: String = (context.get())!!.getString(R.string.pref_units_metric);

            var userPrefersMetric: Boolean = false
            if (metric == preferredUnits) {
                userPrefersMetric = true;
            }

            return userPrefersMetric;
        }
        fun getLocationCoordinates():DoubleArray {
             val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.get());

            val preferredCoordinates = DoubleArray(2)

            /*
             * This is a hack we have to resort to since you can't store doubles in SharedPreferences.
             *
             * Double.doubleToLongBits returns an integer corresponding to the bits of the given
             * IEEE 754 double precision value.
             *
             * Double.longBitsToDouble does the opposite, converting a long (that represents a double)
             * into the double itself.
             */
            preferredCoordinates[0] = Double.fromBits(sp.getLong(PREF_COORD_LAT, 0))
            preferredCoordinates[1] = Double.fromBits(sp.getLong(PREF_COORD_LONG, 0))
            return preferredCoordinates;
        }
        fun areNotificationsEnabled():Boolean {
            /* Key for accessing the preference for showing notifications */
            val displayNotificationsKey:String = context.get()!!.getString(R.string.pref_enable_notifications_key);

            /*
             * In Sunshine, the user has the ability to say whether she would like notifications
             * enabled or not. If no preference has been chosen, we want to be able to determine
             * whether or not to show them. To do this, we reference a bool stored in bools.xml.
             */
            val shouldDisplayNotificationsByDefault: Boolean = context.get()!!
                    .getResources()
                .getBoolean(R.bool.show_notifications_by_default);

            /* As usual, we use the default SharedPreferences to access the user's preferences */
            val sp: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.get());

            /* If a value is stored with the key, we extract it here. If not, use a default. */
            val shouldDisplayNotifications: Boolean = sp
                    .getBoolean(displayNotificationsKey, shouldDisplayNotificationsByDefault);

            return shouldDisplayNotifications;
        }
        fun getLastNotificationTimeInMillis(): Long {
            /* Key for accessing the time at which Sunshine last displayed a notification */
            val lastNotificationKey:String = context.get()!!.getString(R.string.pref_last_notification);

            /* As usual, we use the default SharedPreferences to access the user's preferences */
            val sp:SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context.get());

            /*
             * Here, we retrieve the time in milliseconds when the last notification was shown. If
             * SharedPreferences doesn't have a value for lastNotificationKey, we return 0. The reason
             * we return 0 is because we compare the value returned from this method to the current
             * system time. If the difference between the last notification time and the current time
             * is greater than one day, we will show a notification again. When we compare the two
             * values, we subtract the last notification time from the current system time. If the
             * time of the last notification was 0, the difference will always be greater than the
             * number of milliseconds in a day and we will show another notification.
             */
            val lastNotificationTime:Long = sp.getLong(lastNotificationKey, 0);

            return lastNotificationTime;
        }

    }
}
