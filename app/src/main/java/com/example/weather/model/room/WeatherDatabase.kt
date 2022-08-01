package com.example.weather.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weather.interfaces.WeatherDAO

@Database(entities = [WeatherEntity::class], version = 2)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDAO
}