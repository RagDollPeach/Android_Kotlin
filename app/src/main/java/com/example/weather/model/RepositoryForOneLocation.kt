package com.example.weather.model

import com.example.weather.domain.Weather

fun interface RepositoryForOneLocation {
    fun getWeather(lat :Double ,lon :Double): Weather
}