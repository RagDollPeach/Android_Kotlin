package com.example.weather.model

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.BuildConfig
import com.example.weather.domain.Weather
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.YANDEX_WEATHER_KEY
import com.example.weather.utils.convertDtoToModel
import com.example.weather.utils.getLines
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class RepositoryDetailsWeatherLoaderImpl : RepositoryDetails {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeather(weather: Weather, callBack: MyLargeFatCallBack) {
        Thread {
            val uri =
                URL("https://api.weather.yandex.ru/v2/informers?lat=${weather.city.lat}&lon=${weather.city.lon}")
            var myConnection: HttpsURLConnection? = null
            myConnection = uri.openConnection() as HttpsURLConnection
            try {
                myConnection.readTimeout = 5000
                myConnection.addRequestProperty(YANDEX_WEATHER_KEY, BuildConfig.WEATHER_API_KEY)

                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                callBack.onResponse(convertDtoToModel(weatherDTO, weather))
            } catch (e: IOException) {
                callBack.onError(e)
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}