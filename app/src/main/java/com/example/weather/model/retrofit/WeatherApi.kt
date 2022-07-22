package com.example.weather.model.retrofit

import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.YANDEX_WEATHER_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WeatherApi {
    @GET("/v2/informers")
    fun getWeather(
        @Header(YANDEX_WEATHER_KEY) key: String,
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Call<WeatherDTO>
}