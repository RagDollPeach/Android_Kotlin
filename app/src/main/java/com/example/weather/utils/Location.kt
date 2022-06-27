package com.example.weather.utils

sealed class Location {
    object Russian: Location()
    object World: Location()
}