package com.example.weather.model.dto


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Fact(
    @SerializedName("feels_like")
    val feelsLike: Int,
    val temp: Int,
    @SerializedName("icon")
    val icon: String = "bkn_n"
) : Parcelable
