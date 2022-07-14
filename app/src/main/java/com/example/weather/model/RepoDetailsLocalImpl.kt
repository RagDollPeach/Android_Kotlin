package com.example.weather.model

import com.example.weather.domain.Weather
import com.example.weather.domain.getDefaultCity
import com.example.weather.domain.getRussianCities
import com.example.weather.domain.getWorldCities
import com.example.weather.model.dto.Fact
import com.example.weather.model.dto.WeatherDTO

class RepoDetailsLocalImpl: RepositoryDetails {
    override fun getWeather(lat: Double, lon: Double, callBack: MyLargeFatCallBack) {
        val list = getWorldCities().toMutableList()
        list.addAll(getRussianCities())
        val response = list.filter { it.city.lat == lat && it.city.lon == lon }
        callBack.onResponse(convertModelToDTO(response.first()))
    }

    fun convertDTOToModel(weatherDTO: WeatherDTO): List<Weather> {
        val fact: Fact = weatherDTO.fact
        return listOf(Weather(getDefaultCity(), fact.temp, fact.feelsLike))
    }

    fun convertModelToDTO(weather: Weather): WeatherDTO {
        val fact = Fact(weather.feelsLike,weather.temperature)
        return WeatherDTO(fact)
    }
}