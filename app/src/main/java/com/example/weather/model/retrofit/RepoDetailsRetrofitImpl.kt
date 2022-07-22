package com.example.weather.model.retrofit

import com.example.weather.BuildConfig
import com.example.weather.model.MyLargeFatCallBack
import com.example.weather.model.RepositoryDetails
import com.example.weather.model.dto.WeatherDTO
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RepoDetailsRetrofitImpl: RepositoryDetails {

    override fun getWeather(lat: Double, lon: Double, callBack: MyLargeFatCallBack) {
        val retrofitImpl = Retrofit.Builder()
        retrofitImpl.baseUrl("https://api.weather.yandex.ru")
        retrofitImpl.addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        val api = retrofitImpl.build().create(WeatherApi::class.java)
       // api.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).execute()
        api.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).enqueue(object : Callback<WeatherDTO> {
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if (response.isSuccessful && response.body() != null) {
                    callBack.onResponse(response.body()!!)
                } else {
                    callBack.onError(IOException())
                }
            }

            override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                callBack.onError(t as IOException)
            }
        })
    }
}


