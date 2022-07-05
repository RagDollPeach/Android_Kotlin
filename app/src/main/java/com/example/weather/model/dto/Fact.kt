package com.example.weather.model.dto


import com.google.gson.annotations.SerializedName

data class Fact(
    @SerializedName("feels_like")
    val feelsLike: Int,
    val temp: Int,
)