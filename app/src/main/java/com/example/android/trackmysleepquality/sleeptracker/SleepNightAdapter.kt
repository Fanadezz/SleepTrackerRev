package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding


//Constants to to check item type (i.e. between a header or a SleepNightItem)

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_SLEEP_ITEM = 1

/*class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {*/

/*SleepNightAdapter(val clickListener: SleepNightClickListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>
                                   (SleepNightDiffCallBack()) {*/

/*we needt to the adapter to use any time of ViewHolder not only the SleepNightAdapter. we change the 2nd
Argument from SleepNightAdapter to the generic RecyclerView.ViewHolder*/



class SleepNightAdapter(val clickListener: SleepNightClickListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>
                                   (SleepNightDiffCallBack()) {


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }


    //tells recyclerView how to draw
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {


        return ViewHolder.from(parent)

    }


    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder
                                                                            (binding.root) {


        fun bind(item: SleepNight, clickListener: SleepNightClickListener) {

            binding.sleepNight = item
binding.sleepClickListener = clickListener
            binding.executePendingBindings()

          /*  val res = itemView.context.resources
            binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
            binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)*/
           /* binding.qualityImage.setImageResource(
                )*/
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

class SleepNightClickListener(val clickListener: (id:Long)-> Unit){


    fun onClick(night:SleepNight) = clickListener(night.nightId)
}


sealed class DataItem{

    //abstract val

    abstract val id:Long

    //nested class

    data class SleepNightItem(val sleepNight:SleepNight): DataItem(){
        override val id: Long = sleepNight.nightId

    }
    object Header:DataItem(){


        override val id = Long.MIN_VALUE
    }


}