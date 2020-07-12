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
package com.example.android.sunshine.settingsFragment;



import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import timber.log.Timber
import com.example.android.sunshine.R;
import com.example.android.sunshine.model.preference.SunshinePreferences

/**
 * The SettingsFragment serves as the display for all of the user's settings. In Sunshine, the
 * user will be able to change their preference for units of measurement from metric to imperial,
 * set their preferred weather location, and indicate whether or not they'd like to see
 * notifications.
 *
 * Please note: If you are using our dummy weather services, the location returned will always be
 * Mountain View, California.
 */
// extends PreferenceFragmentCompat implements
//https://developer.android.com/guide/topics/ui/settings/use-saved-values
class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
                setPreferencesFromResource(R.xml.pref_general, rootKey)
        }

        override fun onSharedPreferenceChanged(
                sharedPreferences: SharedPreferences?,
                key: String?
        ) {
                // get acitivity
                //val activity  =  getActivity()
                //indicate key property
                if (key == getString(R.string.pref_location_key)) {
                        // we've changed the location
                        // Wipe out any potential PlacePicker latlng values so that we can use this text entry.
                        SunshinePreferences.resetLocationCoordinates();
                        //SunshineSyncUtils.startImmediateSync(activity);
                }
                else if (key == getString(R.string.pref_units_key)) {
                        // units have changed. update lists of weather entries accordingly
                        Timber.d("change metric settings");
                        //activity?.recreate();

                }
                // units/place change
                val preference: Preference?  = findPreference(key as CharSequence)
                preference?.let {
                        if (!(it is CheckBoxPreference)) {
                                setPreferenceSummary(preference, sharedPreferences?.getString(key, ""));
                        }
                }
        }
        private fun setPreferenceSummary(preference: Preference, value: Any?) {

            val stringValue: String = value.toString()

            if (preference is ListPreference) {
                Timber.i("preference is listprefernce" )
                // For list preferences, look up the correct display value in
                // the preference's 'entries' list (since they have separate labels/values).
                 val listPreference: ListPreference = preference
                // search by value
                val prefIndex: Int = listPreference.findIndexOfValue(stringValue);
                // set preference
                if (prefIndex >= 0) {
                    preference.setSummary(listPreference.getEntries()[prefIndex]);
                } else {
                    Timber.i("not current index "+  prefIndex )
                }
            } else {
                // For other preferences, set the summary to the value's simple string representation.
                preference.setSummary(stringValue);
            }
        }

    override fun onPause() {
        super.onPause()
        getPreferenceScreen().getSharedPreferences()
            .unregisterOnSharedPreferenceChangeListener(this);
    }

    override fun onResume() {
        super.onResume()
        getPreferenceScreen().getSharedPreferences()
            .registerOnSharedPreferenceChangeListener(this);
    }

}
//https://github.com/mlykotom/connecting-the-dots-sample/blob/master/app/src/main/java/com/mlykotom/connectingthedots/presentation/SomeFragment.kt
//https://github.com/android/architecture-samples/blob/dev-dagger/app/src/main/java/com/example/android/architecture/blueprints/todoapp/di/AppComponent.kt
