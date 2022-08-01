package com.example.weather.model.repositories

import android.os.Looper
import android.widget.Toast
import com.example.weather.MyApplication
import com.example.weather.domain.Weather
import com.example.weather.domain.getRussianCities
import com.example.weather.domain.getWorldCities
import com.example.weather.interfaces.MyLargeFatCallBack
import com.example.weather.interfaces.RepositoryDetails

class RepoDetailsLocalImpl: RepositoryDetails {
    override fun getWeather(weather: Weather, callBack: MyLargeFatCallBack) {
       Thread {
           try {
               val list = getWorldCities().toMutableList()
               list.addAll(getRussianCities())
               val response = list.filter { it.city.lat == weather.city.lat && it.city.lon == weather.city.lon }
               callBack.onResponse(response.first())
           } catch (e: NoSuchElementException) {
               Looper.prepare().let { Toast.makeText(MyApplication.getMyApp(),"List is empty", Toast.LENGTH_SHORT).show() }
           }
       }.start()
    }
}