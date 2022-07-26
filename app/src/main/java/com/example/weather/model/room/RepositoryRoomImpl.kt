package com.example.weather.model.room

import android.os.Looper
import android.widget.Toast
import com.example.weather.MyApplication
import com.example.weather.domain.City
import com.example.weather.domain.Weather
import com.example.weather.interfaces.MyLargeFatCallBack
import com.example.weather.interfaces.RepositoryDetails
import com.example.weather.interfaces.RepositoryRoomInsertable

class RepositoryRoomImpl : RepositoryDetails, RepositoryRoomInsertable {

    override fun getWeather(weather: Weather, callBack: MyLargeFatCallBack) {
        Thread {
            try {
                callBack.onResponse(converterEntityToWeather(MyApplication.getWeatherDatabase().weatherDao()
                            .getWeatherByLocation(weather.city.lat, weather.city.lon, weather.icon)).last())

            } catch (ex: NoSuchElementException) {
                Looper.prepare().let { Toast.makeText(MyApplication.getMyApp(), "Этой записи нету в базе данных", Toast.LENGTH_LONG).show() }
            }
        }.start()
    }

    override fun insertWeather(weather: Weather) {
        MyApplication.getWeatherDatabase().weatherDao().insert(converterWeatherToEntity(weather))
    }

    private fun converterEntityToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(
                City(it.name, it.lat, it.lon),
                it.temperature,
                it.feelsLike,
                it.icon
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
            weather.feelsLike,
            weather.icon
        )
    }
}