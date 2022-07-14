package com.example.weather.model

import com.example.weather.domain.Weather

interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double): Weather
}