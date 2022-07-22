package com.example.weather.model

import com.example.weather.domain.Weather
import java.io.IOException

interface MyLargeFatCallBack {
    fun onResponse(weather: Weather)
    fun onError(e: IOException)
}