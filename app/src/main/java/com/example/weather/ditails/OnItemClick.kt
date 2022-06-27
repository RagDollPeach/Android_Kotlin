package com.example.weather.ditails

import com.example.weather.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}