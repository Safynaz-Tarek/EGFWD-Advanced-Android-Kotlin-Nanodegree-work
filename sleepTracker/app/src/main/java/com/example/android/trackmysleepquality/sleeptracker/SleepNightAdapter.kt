package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import androidx.recyclerview.widget.ListAdapter
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

//List adapter helps us build a recycler view that is backed by a list, it will help us
//in keeping track of the list & even notify the adapter when it's updated
//First argument is the type of the list that it is holding,

//We recieve a sleep night listener reference in the constructor, the adapter doesn't
//really care about how clicks get handled, it just takes a callback
class SleepNightAdapter(val clickListener: SleepNightListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
//        val item = getItem(position)
//        holder.bind(item)
    }

    class ViewHolder private constructor(val binding : ListItemSleepNightBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SleepNight, clickListener: SleepNightListener) {
            binding.sleep = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
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

//We'll call onClick whenever the user clicks an item
//this lambda takes data about sleep Night
class SleepNightListener(val clickListener: (sleepId: Long) -> Unit){
    fun onClick(night : SleepNight) = clickListener(night.nightId)
}