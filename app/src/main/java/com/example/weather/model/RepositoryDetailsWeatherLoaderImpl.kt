package com.example.weather.model

import com.example.weather.domain.Weather
import com.example.weather.domain.getDefaultCity

class RepositoryDetailsWeatherLoaderImpl : RepositoryDetails {
    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather(getDefaultCity())
    }
}