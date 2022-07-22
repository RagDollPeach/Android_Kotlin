package com.example.weather.model.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    @SerializedName("fact")
    val fact: Fact,

): Parcelable