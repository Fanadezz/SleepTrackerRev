package com.example.android.trackmysleepquality.sleeptracker

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.android.trackmysleepquality.R
import com.example.android.trackmysleepquality.TextItemViewHolder
import com.example.android.trackmysleepquality.database.SleepNight


class SleepNightAdapter : RecyclerView.Adapter<TextItemViewHolder>(){
var data = mutableListOf<SleepNight>()

         override fun getItemCount(): Int  = data.size

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {

        val item = data[position]
        holder.textView.text = item.sleepQuality.toString()



    }


    //tells recyclerView how to draw
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {


        //get layout inflater from parent view which knows much about themselves
        val inflater = LayoutInflater.from(parent.context)

        //inflate layout

        inflater.inflate(R.layout.)

    }
}