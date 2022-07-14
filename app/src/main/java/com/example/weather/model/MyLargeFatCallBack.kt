package com.example.weather.model

import com.example.weather.model.dto.WeatherDTO
import java.io.IOException

interface MyLargeFatCallBack {
    fun onResponse(weatherDTO: WeatherDTO)
    fun onError(e: IOException)
}