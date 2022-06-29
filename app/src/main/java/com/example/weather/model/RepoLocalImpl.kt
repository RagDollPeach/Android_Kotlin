package com.example.weather.model

import com.example.weather.domain.Weather
import com.example.weather.domain.getRussianCities
import com.example.weather.domain.getWorldCities
import com.example.weather.utils.Location

class RepoLocalImpl : RepositoryForManyLocations, RepositoryForOneLocation {

    override fun getWeatherList(location: Location): List<Weather> {
        return when (location) {
            Location.Russian -> {
                getRussianCities()
            }
            Location.World -> {
                getWorldCities()
            }
        }
    }

    override fun getWeather(lat: Double, lon: Double): Weather {
        return Weather()
    }
}