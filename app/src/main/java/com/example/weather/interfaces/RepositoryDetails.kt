package com.example.weather.interfaces

import com.example.weather.domain.Weather
import com.example.weather.interfaces.MyLargeFatCallBack

interface RepositoryDetails {
    fun getWeather(weather: Weather, callBack: MyLargeFatCallBack)
}