package com.example.weather.view.details

import com.example.weather.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}