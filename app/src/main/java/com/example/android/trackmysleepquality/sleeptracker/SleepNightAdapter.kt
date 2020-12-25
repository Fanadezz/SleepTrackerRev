package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.database.SleepNight
import com.example.android.trackmysleepquality.databinding.ListItemSleepNightBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


//Constants to to check item type (i.e. between a header or a SleepNightItem)

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_SLEEP_ITEM = 1

/*class SleepNightAdapter : RecyclerView.Adapter<SleepNightAdapter.ViewHolder>() {*/

/*SleepNightAdapter(val clickListener: SleepNightClickListener) : ListAdapter<SleepNight, SleepNightAdapter.ViewHolder>
                                   (SleepNightDiffCallBack()) {*/

/*we needt to the adapter to use any time of ViewHolder not only the SleepNightAdapter. we change the 2nd
Argument from SleepNightAdapter to the generic RecyclerView.ViewHolder*/


/*Also the ListAdapter will need to hold a list of Data Items instead of SleepNight*/

class SleepNightAdapter(val clickListener: SleepNightClickListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(
        SleepNightDiffCallBack()) {


    //Coroutine

    val adapterScope = CoroutineScope(Dispatchers.Default)



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val nightItem = getItem(position) as DataItem.SleepNightItem
                holder.bind(nightItem.sleepNight, clickListener)
            }
        }
    }
    //tells recyclerView how to draw
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {


        //return the respective ViewHolder
        return when (viewType) {

            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_SLEEP_ITEM -> ViewHolder.from(parent)

            else -> throw ClassCastException("Unknown ViewType $viewType")
        }


    }

    //override getItemViewType

    override fun getItemViewType(position: Int): Int {
        return when( getItem(position)){

            is DataItem.SleepNightItem -> ITEM_VIEW_TYPE_SLEEP_ITEM

            else -> ITEM_VIEW_TYPE_HEADER
        }
    }


    fun addHeaderAndSubmitList(list: List<SleepNight>?){

        adapterScope.launch {

            val items = when(list){


                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { sleepNight -> DataItem.SleepNightItem(sleepNight) }
            }

            withContext(Main){

                submitList(items)
            }

        }
    }


    class ViewHolder private constructor(val binding: ListItemSleepNightBinding) : RecyclerView.ViewHolder(
            binding.root) {


        fun bind(item:SleepNight,clickListener: SleepNightClickListener) {

            binding.sleepNight = item
            binding.sleepClickListener = clickListener
            binding.executePendingBindings()

            /*  val res = itemView.context.resources
              binding.sleepLength.text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, res)
              binding.qualityString.text = convertNumericQualityToString(item.sleepQuality, res)*/
            /* binding.qualityImage.setImageResource(
                 )*/
        }

        //companion object so that you don't have to instantiate the class to use from()
        companion object {
            fun from(parent: ViewGroup): ViewHolder {


                //get layout inflater from parent view which knows much about themselves
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemSleepNightBinding.inflate(inflater, parent, false)
                //inflate layout
                // val view = inflater.inflate(R.layout.list_item_sleep_night, parent, false)

                //return ViewHolder by calling TexItemViewHolder constructor and passing a view
                return ViewHolder(binding)
            }
        }
    }

        //TextHolder Class
        class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {


            companion object {


                fun from(parent: ViewGroup): RecyclerView.ViewHolder {

                    val inflater = LayoutInflater.from(parent.context)

                    val view = inflater.inflate(R.layout.header, parent, false)

                    return TextViewHolder(view)

                }
            }

        }




    class SleepNightDiffCallBack : DiffUtil.ItemCallback<DataItem>() {
        override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {

           return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
          return oldItem == newItem
        }

    }


}

class SleepNightClickListener(val clickListener: (id: Long) -> Unit) {


    fun onClick(night: SleepNight) = clickListener(night.nightId)
}


sealed class DataItem {

    //abstract val

    abstract val id: Long

    //nested class

    data class SleepNightItem(val sleepNight: SleepNight) : DataItem() {
        override val id: Long = sleepNight.nightId

    }

    object Header : DataItem() {


        override val id = Long.MIN_VALUE
    }


}