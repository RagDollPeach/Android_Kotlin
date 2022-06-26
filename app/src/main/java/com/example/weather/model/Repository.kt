package com.example.weather.model

import com.example.weather.domain.Weather

interface Repository {
    fun getWeatherList(): List<Weather>
    fun getWeather(lat :Double ,lon :Double): Weather
}