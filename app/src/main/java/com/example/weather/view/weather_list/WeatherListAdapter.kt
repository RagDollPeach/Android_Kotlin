package com.example.weather.view.weather_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.databinding.FragmentWeatherListBinding
import com.example.weather.databinding.WeatherListRecycleItemBinding
import com.example.weather.ditails.OnItemClick
import com.example.weather.domain.Weather

class WeatherListAdapter(private val dataList: List<Weather>) :
    RecyclerView.Adapter<WeatherListAdapter.WeatherViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = FragmentWeatherListBinding.inflate(LayoutInflater.from(parent.context))
        return WeatherViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    inner class WeatherViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(weather: Weather) {
            val binding = WeatherListRecycleItemBinding.bind(itemView)
            binding.cityName.text = weather.city.name

        }
    }
}