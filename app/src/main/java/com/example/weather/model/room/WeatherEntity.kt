package com.example.weather.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather_entity_table")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val name: String,
    val lat: Double,
    val lon: Double,
    var temperature: Int = 33,
    var feelsLike: Int = 40
)