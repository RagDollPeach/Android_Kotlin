package com.example.weather.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weather.model.room.WeatherEntity

@Dao
interface WeatherDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntity: WeatherEntity)

    @Query("SELECT * FROM weather_entity_table WHERE lat=:lat AND lon=:lon AND icon=:icon")
    fun getWeatherByLocation(lat: Double, lon: Double,icon: String): List<WeatherEntity>

    @Query("SELECT * FROM weather_entity_table")
    fun getAllWeather(): List<WeatherEntity>
}