package com.example.weather.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.BuildConfig
import com.example.weather.model.dto.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object WeatherLoader {

    @RequiresApi(Build.VERSION_CODES.N)
    fun request(lat: Double, lon: Double, block: (weather: WeatherDTO) -> Unit) {
        try {
            val url = URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}")
            val myConnection: HttpsURLConnection?

            myConnection = url.openConnection() as HttpsURLConnection
            myConnection.readTimeout = 5000
            myConnection.addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
            Thread {
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                block(weatherDTO)
            }.start()
        } catch (e: RuntimeException) {
            e.stackTrace
        }
    }
}