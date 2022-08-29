package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import androidx.recyclerview.widget.ListAdapter
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

//List adapter helps us build a recycler view that is backed by a list, it will help us
//in keeping track of the list & even notify the adapter when it's updated
//First argument is the type of the list that it is holding,
class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight) {

            val res = itemView.context.resources
            binding.sleepLength.text =
                convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)

            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
            binding.qualityImage.setImageResource(
                when (item.sleepQuality) {
                    0 -> R.drawable.ic_sleep_0
                    1 -> R.drawable.ic_sleep_1
                    2 -> R.drawable.ic_sleep_2
                    3 -> R.drawable.ic_sleep_3
                    4 -> R.drawable.ic_sleep_4
                    5 -> R.drawable.ic_sleep_5
                    else -> R.drawable.ic_sleep_0
                }
            )
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}


//DiffUtil is a class we extend to figure out the difference between two items
//
class SleepNightDiffCallback: DiffUtil.ItemCallback<SleepNight>(){

//   This function is used to discover if an item was edited, removed or moved
//    called to check if two objects represent the same item
//    It's important that we only check IDs in this callback
    override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem.nightId == newItem.nightId
    }

//    To determine if an item has changed, this function checks whether the same item have the same data
//    the equality check will check all the fields bc we defined sleepnight as a data class
    override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
        return oldItem == newItem
    }
}