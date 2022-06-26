package com.example.weather.model

import com.example.weather.domain.Weather

class RepoRemoteImpl : Repository {

    override fun getWeatherList(): List<Weather> {
        Thread {
            Thread.sleep(300)
        }.start()
        return listOf(Weather())
    }

    override fun getWeather(lat: Double, lon: Double): Weather {
        Thread {
            Thread.sleep(200)
        }.start()
        return Weather()
    }
}