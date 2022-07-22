package com.example.weather.model

import com.example.weather.domain.Weather
import com.example.weather.domain.getRussianCities
import com.example.weather.domain.getWorldCities
import com.example.weather.utils.Location

class RepoCitiesListImpl : RepositoryCitiesList {

    override fun getCitiesList(location: Location): List<Weather> {
        return when (location) {
            Location.Russian -> {
                getRussianCities()
            }
            Location.World -> {
                getWorldCities()
            }
        }
    }
}