package com.example.weather.view.weatherlist

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.R
import com.example.weather.databinding.FragmentWeatherListBinding
import com.example.weather.domain.Weather
import com.example.weather.utils.NETWORK_ACTION
import com.example.weather.utils.NETWORK_KEY
import com.example.weather.view.details.DetailsFragment
import com.example.weather.view.details.OnItemClick
import com.example.weather.viewmodel.citieslist.CityListFragmentAppState
import com.example.weather.viewmodel.citieslist.CitiesListViewModel
import com.google.android.material.snackbar.Snackbar

class CitiesListFragment : Fragment(), OnItemClick {

    companion object {
        fun getInstance() = CitiesListFragment()
    }

    private var isRussian = true

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
        get() {
            return _binding!!
        }

    private lateinit var viewModel: CitiesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherListBinding.inflate(inflater)
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
                it.getStringExtra(NETWORK_KEY)?.let { str ->
                    Toast.makeText(context,str,Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(CitiesListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }

        binding.floatingButton.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherForRussia()
                binding.floatingButton.setImageResource(R.drawable.ic_earth)
            } else {
                viewModel.getWeatherForWorld()
                binding.floatingButton.setImageResource(R.drawable.ic_russia)
            }
        }
        viewModel.getWeatherForRussia()
        binding.floatingButton.setImageResource(R.drawable.ic_earth)

        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter(NETWORK_ACTION))
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: CityListFragmentAppState) {
        when (appState) {
            is CityListFragmentAppState.Error -> { binding.success()
                binding.root.snackBar(resources.getString(R.string.bar_message), Snackbar.LENGTH_LONG,
                    resources.getString(R.string.action_string)
                    ,Toast.makeText(binding.root.context,"Thank you",Toast.LENGTH_LONG))
                    isRussian = !isRussian
                    if (isRussian) {
                        viewModel.getWeatherForRussia()
                        binding.floatingButton.setImageResource(R.drawable.ic_earth)
                    } else {
                        viewModel.getWeatherForWorld()
                        binding.floatingButton.setImageResource(R.drawable.ic_russia)
                    }
                }

            CityListFragmentAppState.Loading -> {
                binding.loading()
            }
            is CityListFragmentAppState.SuccessForOneLocation -> {
                binding.success()
            }
            is CityListFragmentAppState.SuccessForManyLocations -> {
                binding.success()
                binding.recyclerView.adapter = CitiesListAdapter(appState.weatherList, this)
            }
        }
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager.beginTransaction().hide(this).add(
            R.id.container, DetailsFragment.getInstance(weather)
        ).addToBackStack("").commit()
    }

    private fun FragmentWeatherListBinding.loading() {
        fragmentLoadingLayout.visibility = View.VISIBLE
        floatingButton.visibility = View.GONE
    }

    private fun FragmentWeatherListBinding.success() {
        fragmentLoadingLayout.visibility = View.GONE
        floatingButton.visibility = View.VISIBLE
    }

    private fun View.snackBar(barMessage: String, duration: Int, actionString: String, lambda: Toast) {
        Snackbar.make(this, barMessage, duration).setAction(actionString) { lambda.show() }.show()
    }
}


