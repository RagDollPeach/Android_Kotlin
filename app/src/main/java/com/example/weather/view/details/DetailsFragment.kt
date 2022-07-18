package com.example.weather.view.details

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import com.example.weather.databinding.FragmentDetailsBinding
import com.example.weather.domain.Weather
import com.example.weather.utils.BUNDLE_WEATHER_DTO_KEY
import com.example.weather.viewmodel.details.DetailsFragmentAppState
import com.example.weather.viewmodel.details.DetailsViewModel

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

    private val viewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val weather = arguments?.run {
            getParcelable<Weather>(BUNDLE_WEATHER_DTO_KEY)
        }
        weather?.let { weatherLocal ->
            this.weather = weatherLocal
            viewModel.getWeather(weatherLocal)
            viewModel.getLiveData().observe(viewLifecycleOwner) { renderData(it) }
        }

        val response = requireActivity().getSharedPreferences("repo_check", Context.MODE_PRIVATE)
        val respBody = response.getString("resp","no data").toString()
        printResponse(respBody)
    }

    private fun printResponse(str :String) {
        Toast.makeText(requireContext(),str,Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(detailsFragmentAppState: DetailsFragmentAppState) {
        when (detailsFragmentAppState) {
            is DetailsFragmentAppState.Error -> {}
            DetailsFragmentAppState.Loading -> {}
            is DetailsFragmentAppState.Success -> {
                with(binding) {
                    val weather = detailsFragmentAppState.weatherData
                    cityName.text = weather.city.name
                    temperatureValue.text = weather.temperature.toString()
                    feelsLikeValue.text = weather.feelsLike.toString()
                    cityCoordinates.text = "${weather.city.lat} / ${weather.city.lon}"
                    icon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${weather.icon}.svg")
                }
            }
        }
    }

    private fun ImageView.loadUrl(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .componentRegistry { add(SvgDecoder(this@loadUrl.context)) }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}