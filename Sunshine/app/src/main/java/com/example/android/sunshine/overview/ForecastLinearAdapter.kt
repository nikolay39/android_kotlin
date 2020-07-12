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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.sunshine.App
import com.example.android.sunshine.BR
import com.example.android.sunshine.R
import com.example.android.sunshine.data.database.ForecastEntry
import com.example.android.sunshine.databinding.ExtraWeatherDetailsBinding
import com.example.android.sunshine.databinding.ForecastListItemBinding
import com.example.android.sunshine.databinding.ListItemForecastTodayBinding
import timber.log.Timber

/**
 * This class implements a [RecyclerView] [ListAdapter] which uses Data Binding to present [List]
 * data, including computing diffs between lists.
 * @param onClick a lambda that takes the
 */
const val VIEW_TYPE_TODAY:Int = 0
const val VIEW_TYPE_FUTURE_DAY:Int = 1

class ForecastLinearAdapter(val onClickListener: OnClickListener ) :
        ListAdapter<ForecastEntry, ForecastLinearAdapter.ForecastPropertyViewHolder>(DiffCallback) {
    /**
     * The ForecastPropertyViewHolder constructor takes the binding variable from the associated
     * which nicely gives it access to the full [ForecastEntry] information.
     */
    private var mUseTodayLayout = App.applicationContext().resources.getBoolean(R.bool.use_today_layout)
    class ForecastPropertyViewHolder(private var binding: ViewDataBinding):
            RecyclerView.ViewHolder(binding.root)
    {
        fun bind(forecastEntry: ForecastEntry?) {
            binding.setVariable(BR.property, forecastEntry)
            //binding.property = forecastEntry
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }

    /**
     * Allows the RecyclerView to determine which items have changed when the [List] of [ForecastProperty]
     * has been updated.
     */
    companion object DiffCallback : DiffUtil.ItemCallback<ForecastEntry>() {
        override fun areItemsTheSame(oldItem: ForecastEntry, newItem: ForecastEntry): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: ForecastEntry, newItem: ForecastEntry): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (mUseTodayLayout && position == 0) {
            Timber.d("VIEW_TYPE_TODAY getItem");
            return VIEW_TYPE_TODAY;
        } else {
            Timber.d("VIEW_TYPE_FUTURE_DAY getItem");
            return VIEW_TYPE_FUTURE_DAY;
        }
    }

    /**
     * Create new [RecyclerView] item views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): ForecastLinearAdapter.ForecastPropertyViewHolder {
        when (viewType) {

           VIEW_TYPE_TODAY -> {
               Timber.d("VIEW_TYPE_TODAY layout");
               return ForecastLinearAdapter.ForecastPropertyViewHolder(
                   //ListItemForecastTodayBinding.inflate(LayoutInflater.from(parent.context))
                   ListItemForecastTodayBinding.inflate(LayoutInflater.from(parent.context), parent, false)
               )
            }
            VIEW_TYPE_FUTURE_DAY-> {
                Timber.d("VIEW_TYPE_FUTURE_DAY layout");
                return ForecastLinearAdapter.ForecastPropertyViewHolder(
                    ForecastListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                )

            }
            else -> throw IllegalArgumentException("Invalid view type, value of " + viewType)
        }




    }

    /**
     * Replaces the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ForecastPropertyViewHolder, position: Int) {
        val forecastEntry = getItem(position)
        holder.itemView.setOnClickListener {
            onClickListener.onClick(forecastEntry)
        }
        holder.bind(forecastEntry)
    }

    class OnClickListener(val clickListener: (forecastEntry: ForecastEntry) -> Unit) {
        fun onClick(forecastEntry:ForecastEntry) = clickListener(forecastEntry)
    }
}
