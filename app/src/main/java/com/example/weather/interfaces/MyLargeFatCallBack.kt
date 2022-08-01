package com.example.weather.interfaces

import com.example.weather.domain.Weather

interface MyLargeFatCallBack {
    fun onResponse(weather: Weather)
    fun onError(e: Exception)
}