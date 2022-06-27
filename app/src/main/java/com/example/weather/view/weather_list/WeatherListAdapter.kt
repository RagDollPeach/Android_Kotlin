package com.example.weather.view.weather_list

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.domain.Weather

class WeatherListAdapter(val dataList: List<Weather>):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
       return dataList.size
    }

}