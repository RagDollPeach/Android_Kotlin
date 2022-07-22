package com.example.weather.viewmodel.details

import com.example.weather.model.dto.WeatherDTO

sealed class DetailsFragmentAppState {
    data class Success(val weatherData: WeatherDTO) : DetailsFragmentAppState()
    data class Error(val error: Throwable) : DetailsFragmentAppState()
    object Loading : DetailsFragmentAppState()
}