package com.example.weather.view.ditails

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.weather.databinding.FragmentDetailsBinding
import com.example.weather.domain.Weather
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.getLines
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class DetailsFragment : Fragment() {

    companion object {
        const val BUNDLE_WEATHER_BALTIC = "agmdgfgs "
        fun getInstance(weather: Weather) = DetailsFragment().apply {
            arguments = Bundle().also { it.putParcelable(BUNDLE_WEATHER_BALTIC, weather) }
        }
    }

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.run {
            getParcelable<Weather>(BUNDLE_WEATHER_BALTIC)
        }
        weather?.let { renderData(weather) }

        weather?.let {

            val url =
                URL("https://api.weather.yandex.ru/v2/forecast?lat=${it.city.lat}&lon=${it.city.lon}")
            val myConnection: HttpsURLConnection?

            myConnection = url.openConnection() as HttpsURLConnection
            myConnection.readTimeout = 5000
            myConnection.addRequestProperty("X-Yandex-API-Key","c21646d7-714b-44c7-b798-a0b1b09fb340")

            Thread {
                val reader = BufferedReader(InputStreamReader(myConnection.inputStream))
                val weatherDTO = Gson().fromJson(getLines(reader), WeatherDTO::class.java)

                requireActivity().runOnUiThread {
                    renderData(it.apply {
                        feelsLike = weatherDTO.fact.feelsLike
                        temperature = weatherDTO.fact.temp
                    })
                }
            }.start()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun renderData(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            cityCoordinates.text = "${weather.city.lat} / ${weather.city.lon}"
        }
    }
}