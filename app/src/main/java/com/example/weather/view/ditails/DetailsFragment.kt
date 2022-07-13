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
import com.example.weather.utils.BUNDLE_WEATHER_BALTIC
import com.example.weather.utils.WeatherLoader

class DetailsFragment : Fragment() {

    companion object {
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
            WeatherLoader.request(weather.city.lat, weather.city.lon) { weatherDTO ->
                requireActivity().runOnUiThread {
                    renderData(it.apply {
                        feelsLike = weatherDTO.fact.feelsLike
                        temperature = weatherDTO.fact.temp
                    })
                }
            }
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