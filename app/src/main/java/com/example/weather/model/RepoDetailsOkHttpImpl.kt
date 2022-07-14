package com.example.weather.model

import android.util.Log
import com.example.weather.BuildConfig
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.YANDEX_WEATHER_KEY
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class RepoDetailsOkHttpImpl : RepositoryDetails {
    override fun getWeather(lat: Double, lon: Double, callBack: MyLargeFatCallBack){

        val client = OkHttpClient()
        val builder = Request.Builder()
        builder.addHeader(YANDEX_WEATHER_KEY, BuildConfig.WEATHER_API_KEY)
        builder.url(
            "https://api.weather.yandex.ru/v2/informers?" +
                    "lat=${lat}&lon=${lon}"
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
                        callBack.onResponse(weatherDTO)
                    }
                }
            }
        })
    }
}



