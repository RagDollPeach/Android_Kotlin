package com.example.weather.model.repositories

import android.util.Log
import com.example.weather.BuildConfig
import com.example.weather.domain.Weather
import com.example.weather.interfaces.MyLargeFatCallBack
import com.example.weather.interfaces.RepositoryDetails
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.YANDEX_WEATHER_KEY
import com.example.weather.utils.convertDtoToModel
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RepoDetailsOkHttpImpl : RepositoryDetails {
    override fun getWeather(weather: Weather, callBack: MyLargeFatCallBack) {
        Thread {
            val client = OkHttpClient()
            val builder = Request.Builder()
            builder.addHeader(YANDEX_WEATHER_KEY, BuildConfig.WEATHER_API_KEY)
            builder.url(
                "https://api.weather.yandex.ru/v2/informers?" +
                        "lat=${weather.city.lat}&lon=${weather.city.lon}"
            )
            val request: Request = builder.build()
            val call: Call = client.newCall(request)

            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("@@@", "${e.printStackTrace()}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful && response.body != null) {
                        response.body?.let {
                            val responseString = it.string()
                            val weatherDTO = Gson().fromJson(responseString, WeatherDTO::class.java)
                            callBack.onResponse(convertDtoToModel(weatherDTO, weather))
                        }
                    }
                }
            })
        }.start()
    }

}



