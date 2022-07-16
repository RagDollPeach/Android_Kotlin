package com.example.weather.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_entity_table WHERE lat=:lat AND lon=:lon")
    fun getWeatherByLocation(lat: Double, lon: Double): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity_table")
    fun getAllWeather(): List<WeatherEntity>
}