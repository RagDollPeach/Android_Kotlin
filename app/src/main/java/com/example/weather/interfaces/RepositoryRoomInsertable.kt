package com.example.weather.interfaces

import com.example.weather.domain.Weather

fun interface RepositoryRoomInsertable {
    fun insertWeather(weather: Weather)
}