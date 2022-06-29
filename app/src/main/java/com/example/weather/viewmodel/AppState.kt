package com.example.weather.viewmodel

import com.example.weather.domain.Weather

sealed class AppState {
    data class SuccessForManyLocations(val weatherList: List<Weather>) : AppState()
    data class SuccessForOneLocation(val weatherData: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}