package com.example.weather

import android.app.Application
import androidx.room.Room
import com.example.weather.model.room.WeatherDatabase
import com.example.weather.utils.ROOM_DATABASE

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        myApplication = this
    }

    companion object {
        private var myApplication: MyApplication? = null
        private var weatherDatabase: WeatherDatabase? = null
        fun getMyApp() = myApplication!!

        fun getWeatherDatabase(): WeatherDatabase {
            if (weatherDatabase == null) {
                weatherDatabase =
                    Room.databaseBuilder(getMyApp(), WeatherDatabase::class.java, ROOM_DATABASE)
                        .allowMainThreadQueries()// не получилось избавится от этого метода
                        .build()
            }
            return weatherDatabase!!
        }
    }
}