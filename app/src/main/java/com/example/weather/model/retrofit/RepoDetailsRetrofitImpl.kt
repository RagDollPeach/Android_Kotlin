package com.example.weather.model.retrofit

import com.example.weather.BuildConfig
import com.example.weather.MyApplication
import com.example.weather.domain.Weather
import com.example.weather.model.MyLargeFatCallBack
import com.example.weather.model.RepositoryDetails
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.convertDtoToModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class RepoDetailsRetrofitImpl: RepositoryDetails {

    override fun getWeather(weather: Weather, callBack: MyLargeFatCallBack) {
       // api.getWeather(BuildConfig.WEATHER_API_KEY,lat,lon).execute()
        MyApplication.retrofit.getWeather(BuildConfig.WEATHER_API_KEY,weather.city.lat,weather.city.lon).enqueue(object : Callback<WeatherDTO> {
            override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                if (response.isSuccessful && response.body() != null) {
                    callBack.onResponse(convertDtoToModel(response.body()!!,weather))
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


