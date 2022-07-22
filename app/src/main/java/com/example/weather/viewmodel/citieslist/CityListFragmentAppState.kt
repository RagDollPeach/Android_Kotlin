package com.example.weather.viewmodel.citieslist

import com.example.weather.domain.Weather

sealed class CityListFragmentAppState {
    data class SuccessForManyLocations(val weatherList: List<Weather>) : CityListFragmentAppState()
    data class SuccessForOneLocation(val weatherData: Weather) : CityListFragmentAppState()
    data class Error(val error: Throwable) : CityListFragmentAppState()
    object Loading : CityListFragmentAppState()
}