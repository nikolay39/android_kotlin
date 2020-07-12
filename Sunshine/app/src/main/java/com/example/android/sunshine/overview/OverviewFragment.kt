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

package com.example.android.sunshine.overview

import com.example.android.sunshine.di.modules.viewmodel.InjectingSavedStateViewModelFactory
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.android.sunshine.App
import com.example.android.sunshine.R
import com.example.android.sunshine.common.EventObserver
import com.example.android.sunshine.data.database.ForecastEntry
import com.example.android.sunshine.databinding.FragmentOverviewBinding
import com.example.android.sunshine.model.preference.SunshinePreferences
import com.example.android.sunshine.settingsFragment.SettingsActivity
import timber.log.Timber
import javax.inject.Inject


/**
 * This fragment shows the the status of the Mars real-estate web services transaction.
 */
const val TAG: String = "OVERVIEW"
class OverviewFragment : Fragment()  {

    @Inject
    lateinit var abstractViewModelFactory: InjectingSavedStateViewModelFactory

    lateinit var viewModel: OverviewViewModel

    //private lateinit var binding :FragmentOverviewBinding
    private lateinit var listener: SharedPreferences.OnSharedPreferenceChangeListener
    private lateinit var binding: FragmentOverviewBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // dagger ViewModel


    }
    /**
     * Inflates the layout with Data Binding, sets its lifecycle owner to the OverviewFragment
     * to enable Data Binding to observe LiveData, and sets up the RecyclerView with an adapter.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (requireActivity().applicationContext as App).appComponent.inject(this)

        val factory = abstractViewModelFactory.create(this)
        viewModel = ViewModelProvider(this, factory)[OverviewViewModel::class.java]
        val binding = FragmentOverviewBinding.inflate(inflater)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.setLifecycleOwner(this)

        // Giving the binding access to the OverviewViewModel
        binding.viewModel = viewModel

        // Sets the adapter of the photosGrid RecyclerView with clickHandler lambda that
        // tells the viewModel when our property is clicked
        binding.recyclerviewForecast.adapter = ForecastLinearAdapter(ForecastLinearAdapter.OnClickListener {
            viewModel.displayPropertyDetails(it)
        })
        viewModel.openDetailForecastEvent.observe(this.viewLifecycleOwner, EventObserver {
                // Must find the NavController from the Fragment
                this.findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(it))
                // Tell the ViewModel we've made the navigate call to prevent multiple navigation
        })
        // Observe the navigateToSelectedProperty LiveData and Navigate when it isn't null
        // After navigating, call displayPropertyDetailsComplete() so that the ViewModel is ready
        // for another navigation event.

        setHasOptionsMenu(true)
        listener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences_,key ->
                if (key == getString(R.string.pref_units_key)) {
                    binding.recyclerviewForecast.adapter!!.notifyDataSetChanged()
                    Timber.d("Change settings DetailActivity");
                }
            }
        PreferenceManager.getDefaultSharedPreferences(this.context)
            .registerOnSharedPreferenceChangeListener(listener)
        Timber.i("onCreateView create timber")
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this.context)
            .unregisterOnSharedPreferenceChangeListener(listener)
    }

    /**
     * Inflates the overflow menu that contains filtering options.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.forecast, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
          // Todo navigation
          when(item.itemId){
            R.id.action_map ->  {
                openPreferredLocationInMap();
            }
            // Todo change Navigation
            R.id.action_settings-> {
                startActivity(Intent(activity, SettingsActivity::class.java))
            }
        }
        return true

    }
    private fun openPreferredLocationInMap() {
        val coords = SunshinePreferences.getLocationCoordinates();
        val posLat: String = coords[0].toString()
        val posLong: String = coords[1].toString()
        val geoLocation: Uri = Uri.parse("geo:" + posLat + "," + posLong);

        val intent: Intent = Intent(Intent.ACTION_VIEW);
        intent.setData(geoLocation)
        if (intent.resolveActivity(requireActivity().getPackageManager()) != null) {
            Timber.d("posLat " + posLat);
            Timber.d("posLong " + posLong);
            startActivity(intent);
        } else {
            Timber.tag(TAG).d("Couldn't call " + geoLocation.toString() + ", no receiving apps installed!");
        }
    }
}
