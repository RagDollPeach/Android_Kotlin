package com.example.weather.view.weather_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.databinding.FragmentWeatherListBinding
import com.example.weather.view_model.AppState

class WeatherListFragment : Fragment() {

    companion object {
        fun getInstance() = WeatherListFragment()
    }

    private lateinit var binding: FragmentWeatherListBinding
    private lateinit var viewModel: WeatherListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeatherListBinding.inflate(inflater)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }
        viewModel.sendRequest()
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
               throw IllegalStateException()
            }
            AppState.Loading -> {
                Toast.makeText(requireContext(), "Загруска $appState", Toast.LENGTH_LONG).show()
            }
            is AppState.Success -> {
                val result = appState.weatherData
                binding.cityName.text = result.city.city
                binding.temperatureValue.text = result.temperature.toString()
                binding.cityCoordinates.text = "${result.city.lat} - ${result.city.lon}"
                binding.feelsLikeValue.text = result.feelsLike.toString()
            }
        }
    }
}