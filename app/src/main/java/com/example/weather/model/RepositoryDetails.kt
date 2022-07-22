package com.example.weather.model

import com.example.weather.domain.Weather

interface RepositoryDetails {
    fun getWeather(weather: Weather, callBack: MyLargeFatCallBack)
}