package com.example.weather.view.ditails

import com.example.weather.domain.Weather

fun interface OnItemClick {
    fun onItemClick(weather: Weather)
}