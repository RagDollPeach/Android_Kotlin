package com.example.weather.interfaces

import com.example.weather.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}