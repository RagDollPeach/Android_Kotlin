package com.example.weather.view.ditails

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weather.databinding.FragmentDetailsBinding
import com.example.weather.domain.Weather

class DetailsFragment : Fragment() {

    companion object {
        const val BUNDLE_WEATHER_BALTIC = "agmdgfgs "
        fun getInstance(weather: Weather) = DetailsFragment().apply {
               arguments = Bundle().also{ it.putParcelable(BUNDLE_WEATHER_BALTIC, weather) }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.run {
            getParcelable<Weather>(BUNDLE_WEATHER_BALTIC)
                ?.let { weather -> renderData(weather) }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            temperatureValue.text = weather.temperature.toString()
            feelsLikeValue.text = weather.feelsLike.toString()
            cityCoordinates.text = "${weather.city.lat} - ${weather.city.lon}"
        }
    }
}