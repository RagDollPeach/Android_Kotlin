package com.example.weather.view.details

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.BuildConfig
import com.example.weather.domain.City
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.*
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsServiceIntent : IntentService("intent_service") {

    @Deprecated("Deprecated in Java")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            it.getParcelableExtra<City>(BUNDLE_CITY_KEY)?.let { city ->
                try {
                    val url =
                        URL("https://api.weather.yandex.ru/v2/informers?lat=${city.lat}&lon=${city.lon}")
                    Thread {
                        val myConnection: HttpsURLConnection?
                        myConnection = url.openConnection() as HttpsURLConnection
                        try {
                            myConnection.readTimeout = 5000
                            myConnection.addRequestProperty(YANDEX_WEATHER_KEY, BuildConfig.WEATHER_API_KEY)

                            val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                            val weatherDTO =
                                Gson().fromJson(getLines(reader), WeatherDTO::class.java)

                            LocalBroadcastManager.getInstance(this).sendBroadcast(Intent().apply {
                                putExtra(BUNDLE_WEATHER_DTO_KEY, weatherDTO)
                                action = WAVE
                            })
                        } catch (e: RuntimeException) {
                            e.stackTrace
                        } catch (e: IOException) {
                            e.stackTrace
                        } catch (e: JsonSyntaxException) {
                            e.stackTrace
                        } finally {
                            myConnection.disconnect()
                        }
                    }.start()
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                }
            }
        }
    }
}