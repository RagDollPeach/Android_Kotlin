package com.example.weather.model

import android.widget.Toast
import com.example.weather.MyApplication
import com.example.weather.domain.City
import com.example.weather.domain.Weather
import com.example.weather.model.room.WeatherEntity

class RepositoryRoomImpl : RepositoryDetails, RepositoryRoomInsertable {

    override fun getWeather(weather: Weather, callBack: MyLargeFatCallBack) {
        try {
            callBack.onResponse(converterEntityToWeather(
                MyApplication.getWeatherDatabase().weatherDao()
                    .getWeatherByLocation(weather.city.lat, weather.city.lon)).last())
        } catch (ex: NoSuchElementException) {
            Toast.makeText(MyApplication.getMyApp(),"Этой записи нету в базе данных", Toast.LENGTH_LONG).show()
        }
    }

    override fun insertWeather(weather: Weather) {
        MyApplication.getWeatherDatabase().weatherDao().insert(converterWeatherToEntity(weather))
    }

    private fun converterEntityToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(
                City(it.name, it.lat, it.lon),
                it.temperature,
                it.feelsLike
            )
        }
    }

    private fun converterWeatherToEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(
            0,
            weather.city.name,
            weather.city.lat,
            weather.city.lon,
            weather.temperature,
            weather.feelsLike
        )
    }
}