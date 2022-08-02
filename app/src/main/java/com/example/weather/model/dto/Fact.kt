package com.example.weather.model.dto


import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


@Keep
data class Fact(
    @SerializedName("feels_like")
    val feelsLike: Int,
    val temp: Int,
    @SerializedName("icon")
    val icon: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString()
    )

    override fun describeContents(): Int {
        return -1
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeInt(feelsLike)
        p0?.writeInt(temp)
        p0?.writeString(icon)
    }

    companion object CREATOR : Parcelable.Creator<Fact> {
        override fun createFromParcel(parcel: Parcel): Fact {
            return Fact(parcel)
        }

        override fun newArray(size: Int): Array<Fact?> {
            return arrayOfNulls(size)
        }
    }
}