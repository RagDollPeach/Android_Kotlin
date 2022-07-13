package com.example.weather.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.weather.BuildConfig
import com.example.weather.model.dto.WeatherDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.FileNotFoundException
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

object WeatherLoader {

    @RequiresApi(Build.VERSION_CODES.N)
    fun request(lat: Double, lon: Double, block: (weather: WeatherDTO) -> Unit) {
        val url = URL("https://api.weather.yandex.ru/v2/informers?lat=${lat}&lon=${lon}")
        Thread {
            val myConnection: HttpsURLConnection?
            myConnection = url.openConnection() as HttpsURLConnection
            try {
                myConnection.readTimeout = 5000
                myConnection.addRequestProperty(YANDEX_WEATHER_KEY, BuildConfig.WEATHER_API_KEY)
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)
                block(weatherDTO)
            } catch (e: Exception) {
                e.stackTrace
            } finally {
                myConnection.disconnect()
            }
        }.start()
    }
}