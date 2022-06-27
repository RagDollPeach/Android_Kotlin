package com.example.weather.model

import com.example.weather.domain.Weather
import com.example.weather.utils.Location

fun interface RepositoryForManyLocations {
    fun getWeatherList(location: Location): List<Weather>
}