package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.convertDurationToFormatted
import com.example.android.trackmysleepquality.convertNumericQualityToString
import com.example.android.trackmysleepquality.database.SleepNight

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


    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val sleepLength = itemView.findViewById<TextView>(R.id.sleep_length)
        val quality = itemView.findViewById<TextView>(R.id.quality_string)
        val qualityImage = itemView.findViewById<ImageView>(R.id.quality_image)


        fun bind(item: SleepNight) {

            val res = itemView.context.resources
            sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            quality.text = convertNumericQualityToString(item.sleepQuality, res)
            qualityImage.setImageResource(
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

                //inflate layout
                val view = inflater.inflate(R.layout.list_item_sleep_night, parent, false)

                //return ViewHolder by calling TexItemViewHolder constructor and passing a view
                return ViewHolder(view)
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