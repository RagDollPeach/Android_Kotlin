package com.example.weather.model

import com.example.weather.domain.Weather

class RepoLocalImpl : Repository {
    override fun getWeatherList(): List<Weather> {
        return listOf(Weather())
    }

    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }
}