package com.example.weather.model

import com.example.weather.domain.Weather

interface MyLargeFatCallBack {
    fun onResponse(weather: Weather)
    fun onError(e: Exception)
}