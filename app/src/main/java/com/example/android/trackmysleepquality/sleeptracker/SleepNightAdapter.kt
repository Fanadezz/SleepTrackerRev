package com.example.android.trackmysleepquality.sleeptracker

import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight


class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>(){
var data = mutableListOf<SleepNight>()

         override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {

        val elementAtPosition = data[position]
        holder.textView.text = elementAtPosition.sleepQuality.toString()



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        TODO("Not yet implemented")
    }
}