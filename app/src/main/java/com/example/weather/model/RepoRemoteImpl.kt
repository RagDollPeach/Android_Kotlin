package com.example.weather.model

import com.example.weather.domain.Weather

class RepoRemoteImpl : RepositoryForOneLocation {

    override fun getWeather(lat: Double, lon: Double): Weather {
        Thread {
            Thread.sleep(200)
        }.start()
        return Weather()
    }
}