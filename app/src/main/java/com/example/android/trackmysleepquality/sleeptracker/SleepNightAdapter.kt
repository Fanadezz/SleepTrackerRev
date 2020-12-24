package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding

/*class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {*/

class SleepNightAdapter : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>(SleepNightDiffCallBack()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    //tells recyclerView how to draw
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder.from(parent)

    }


    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder
                                                                            (binding.root) {


        fun bind(item: SleepNight) {

            val res = itemView.context.resources
            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)
            binding.qualityImage.setImageResource(
                    when (item.sleepQuality) {
                        0    -> R.drawable.ic_sleep_0
                        1    -> R.drawable.ic_sleep_1
                        2    -> R.drawable.ic_sleep_2
                        3    -> R.drawable.ic_sleep_3
                        4    -> R.drawable.ic_sleep_4
                        5    -> R.drawable.ic_sleep_5
                        else -> R.drawable.ic_sleep_active
                    })
        }


        companion object {
            fun from(parent: ViewGroup): ViewHolder {



                //get layout inflater from parent view which knows much about themselves
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(inflater,parent,false)
                //inflate layout
               // val view = inflater.inflate(R.layout.list_item_sleep_night, parent, false)

                //return ViewHolder by calling TexItemViewHolder constructor and passing a view
                return ViewHolder(binding)
            }
        }

    }


    class SleepNightDiffCallBack : DiffUtil.ItemCallback<SleepNight>() {
        override fun areItemsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem.nightId == newItem.nightId
        }

        override fun areContentsTheSame(oldItem: SleepNight, newItem: SleepNight): Boolean {
            return oldItem == newItem
        }
    }


}