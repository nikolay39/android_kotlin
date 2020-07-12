/*
 *  Copyright 2018, The Android Open Source Project
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.example.android.sunshine.detail

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.example.android.sunshine.App
import com.example.android.sunshine.databinding.FragmentDetailBinding
import com.example.android.sunshine.detail.DetailViewModel
import com.example.android.sunshine.R
import com.example.android.sunshine.data.database.ForecastEntry
import com.example.android.sunshine.di.modules.viewmodel.InjectingSavedStateViewModelFactory
import com.example.android.sunshine.overview.OverviewViewModel
import com.example.android.sunshine.settingsFragment.SettingsActivity
import dagger.android.support.AndroidSupportInjection.inject
import timber.log.Timber
import javax.inject.Inject


/**
 * This [Fragment] shows the detailed information about a selected piece of Mars real estate.
 * It sets this information in the [DetailViewModel], which it gets as a Parcelable property
 * through Jetpack Navigation's SafeArgs.
 */
class DetailFragment : Fragment() {
    private val args: DetailFragmentArgs by navArgs()

    @Inject
    lateinit var abstractViewModelFactory: InjectingSavedStateViewModelFactory
    lateinit var viewModel: DetailViewModel


    private lateinit var binding: FragmentDetailBinding
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (requireActivity().applicationContext as App).appComponent.inject(this)
        val factory = abstractViewModelFactory.create(this, arguments)

        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]
        binding = FragmentDetailBinding.inflate(inflater)
        binding.setLifecycleOwner(this)

        binding.primaryInfo.property = viewModel.selectedProperty.value
        binding.extraDetails.property = viewModel.selectedProperty.value
        setHasOptionsMenu(true)
        listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences_,key ->
                if (key == getString(R.string.pref_units_key)) {
                    binding.invalidateAll()
                    Timber.d("Change settings DetailActivity");
                }
            }
        PreferenceManager.getDefaultSharedPreferences(this.context)
            .registerOnSharedPreferenceChangeListener(listener)
        return binding.root
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)


    }
    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this.context)
            .unregisterOnSharedPreferenceChangeListener(listener)
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.detail, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Todo navigation
        when(item.itemId){
            R.id.action_share ->  {
                val shareIntent: Intent = createShareForecastIntent()
                startActivity(shareIntent)
            }
            // Todo change Navigation
            R.id.action_settings-> {
                startActivity(Intent(activity, SettingsActivity::class.java))
            }
        }
        return true

    }
    private fun createShareForecastIntent(): Intent {
        val shareIntent: Intent = ShareCompat.IntentBuilder.from(activity)
            .setType("text/plain")
            .setText(setSummary())
            .getIntent()
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT)
        return shareIntent
    }
    private fun setSummary(): String {
        val mForecastSummaryString = String.format("%s - %s - %s/%s",
            binding.primaryInfo.date.getText(),
            binding.primaryInfo.weatherDescription.getText(),
            binding.primaryInfo.highTemperature.getText(),
            binding.primaryInfo.lowTemperature.getText());
        return mForecastSummaryString
    }
}
