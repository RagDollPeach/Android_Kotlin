package com.example.weather.model

interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double, callBack: MyLargeFatCallBack)
}