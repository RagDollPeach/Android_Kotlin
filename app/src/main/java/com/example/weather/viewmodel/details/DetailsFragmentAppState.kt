package com.example.weather.viewmodel.details

import com.example.weather.domain.Weather

sealed class DetailsFragmentAppState {
    data class Success(val weatherData: Weather) : DetailsFragmentAppState()
    data class Error(val error: Throwable) : DetailsFragmentAppState()
    object Loading : DetailsFragmentAppState()
}