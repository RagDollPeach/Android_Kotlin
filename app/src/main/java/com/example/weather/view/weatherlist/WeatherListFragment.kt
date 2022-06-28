package com.example.weather.view.weatherlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherListBinding
import com.example.weather.ditails.OnItemClick
import com.example.weather.domain.Weather
import com.example.weather.viewmodel.AppState

class WeatherListFragment : Fragment(), OnItemClick {

    companion object {
        fun getInstance() = WeatherListFragment()
    }

    var isRussian = true

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

        binding.floatingButton.setOnClickListener{
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherForRussia()
                binding.floatingButton.setImageResource(R.drawable.ic_russia)
            } else {
                viewModel.getWeatherForWorld()
                binding.floatingButton.setImageResource(R.drawable.ic_earth)
            }
        }
        viewModel.getWeatherForRussia()
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Error -> {
               throw IllegalStateException()
            }
            AppState.Loading -> {
               // Toast.makeText(requireContext(), "Загруска $appState", Toast.LENGTH_LONG).show()
            }
            is AppState.SuccessForOneLocation -> {
                val result = appState.weatherData

            }
            is AppState.SuccessForManyLocations -> {
                binding.recyclerView.adapter = WeatherListAdapter(appState.weatherList)
            }
        }
    }

    override fun onItemClick(weather: Weather) {
//        requireActivity().supportFragmentManager.beginTransaction().hide(this).add(
//            R.id.container, DetailsFragment.newInstance(weather)
//        ).addToBackStack("").commit()
    }
}