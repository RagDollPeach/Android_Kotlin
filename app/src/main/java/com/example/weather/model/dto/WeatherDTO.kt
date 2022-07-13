package com.example.weather.model.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    @SerializedName("fact")
    val fact: Fact,
    @SerializedName("info")
    val info: Info,
    @SerializedName("now")
    val now: Int,
    @SerializedName("now_dt")
    val nowDt: String,
): Parcelable