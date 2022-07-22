package com.example.weather.model

import com.example.weather.domain.Weather

fun interface RepositoryRoomInsertable {
    fun insertWeather(weather: Weather)
}