package com.example.weather.view.details

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.BuildConfig
import com.example.weather.databinding.FragmentDetailsBinding
import com.example.weather.domain.Weather
import com.example.weather.model.dto.WeatherDTO
import com.example.weather.utils.*
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException

class DetailsFragment : Fragment() {

    companion object {
        fun getInstance(weather: Weather) = DetailsFragment().apply {
            arguments = Bundle().also { it.putParcelable(BUNDLE_WEATHER_DTO_KEY, weather) }
        }
    }

    lateinit var weather: Weather

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
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                it.getParcelableExtra<WeatherDTO>(BUNDLE_WEATHER_DTO_KEY)?.let { weatherDTO ->
                    bindWeatherDTO(weather, weatherDTO)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.run {
            getParcelable<Weather>(BUNDLE_WEATHER_DTO_KEY)
        }
        weather?.let { weatherLocal ->
            this.weather = weatherLocal
//            renderData(weather)
//
//            LocalBroadcastManager.getInstance(requireContext())
//                .registerReceiver(receiver, IntentFilter(WAVE))
//
//            requireActivity().startService(
//                Intent(requireContext(), DetailsServiceIntent::class.java).apply {
//                    putExtra(BUNDLE_CITY_KEY, weather.city)
//                })

            val client = OkHttpClient()
            val builder = Request.Builder()
            builder.addHeader(YANDEX_WEATHER_KEY, BuildConfig.WEATHER_API_KEY)
            builder.url("https://api.weather.yandex.ru/v2/informers?" +
                    "lat=${weatherLocal.city.lat}&lon=${weatherLocal.city.lon}")
            val request: Request = builder.build()
            val call: Call = client.newCall(request)

            call.enqueue(object : Callback{
                override fun onFailure(call: Call, e: IOException) {
                    Log.d("@@@", "${e.printStackTrace()}")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful && response.body != null) {
                        response.body?.let {
                            val responseString = it.string()
                            val weatherDTO = Gson().fromJson(responseString, WeatherDTO::class.java)
                            weatherLocal.feelsLike = weatherDTO.fact.feelsLike
                            weatherLocal.temperature = weatherDTO.fact.temp
                            requireActivity().runOnUiThread {
                                renderData(weatherLocal)
                            }
                        }
                    }
                }
            })
        }
    }

    private fun bindWeatherDTO(weather: Weather, weatherDTO: WeatherDTO) {
        requireActivity().runOnUiThread {
            renderData(weather.apply {
                weather.feelsLike = weatherDTO.fact.feelsLike
                weather.temperature = weatherDTO.fact.temp
            })
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