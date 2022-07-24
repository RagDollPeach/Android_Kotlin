package com.example.weather.view.weatherlist

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.R
import com.example.weather.databinding.FragmentCityListBinding
import com.example.weather.domain.City
import com.example.weather.domain.Weather
import com.example.weather.interfaces.OnItemClick
import com.example.weather.utils.NETWORK_ACTION
import com.example.weather.utils.NETWORK_KEY
import com.example.weather.utils.REQUEST_CODE_FOR_LOCATION
import com.example.weather.view.details.DetailsFragment
import com.example.weather.viewmodel.citieslist.CitiesListViewModel
import com.example.weather.viewmodel.citieslist.CityListFragmentAppState
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_city_list.*
import java.util.*


class CitiesListFragment : Fragment(), OnItemClick {

    companion object {
        fun getInstance() = CitiesListFragment()
    }

    lateinit var locationManager: LocationManager

    private var isRussian = true

    private var _binding: FragmentCityListBinding? = null
    private val binding: FragmentCityListBinding
        get() {
            return _binding!!
        }

    private lateinit var viewModel: CitiesListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCityListBinding.inflate(inflater)
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
                    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(CitiesListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner) { t -> renderData(t) }
        viewModel.getWeatherForRussia()

        floatingButtonManager()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(receiver, IntentFilter(NETWORK_ACTION))

        radioGroupManager()

        binding.floatingMapsButton.setOnClickListener {
            checkPermission()
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    2000L,
                    0f, locationListener)
            } else {
                Toast.makeText(requireContext(), "Включите локализацию", Toast.LENGTH_LONG).show()
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            getAddress(location)
        }

        override fun onProviderDisabled(provider: String) {
            Log.d("@@@", "Гео локация выключена")
            super.onProviderDisabled(provider)
        }

        override fun onProviderEnabled(provider: String) {
            Log.d("@@@", "Гео локация включена")
            super.onProviderEnabled(provider)
        }
    }

    fun getAddress(location: Location) {
        val geocoder = Geocoder(context, Locale("ru_RU"))
        Thread {
            try {
                val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                locationManager.removeUpdates(locationListener)
                onItemClick(Weather(City(address.first().locality, location.latitude, location.longitude)))
            } catch (e: NullPointerException) {
                Looper.prepare().let {
                    Toast.makeText(requireContext(), R.string.location_message, Toast.LENGTH_LONG).show() }
            }
        }.start()
    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder(requireContext())
                .setTitle("Доступ к гео локации")
                .setMessage("Дайте доступ приложению к гео локации")
                .setPositiveButton("Дать") { _, _ ->
                    permissionRequest()
                }
                .setNegativeButton("Не дать") { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest()
        }
    }

    private fun permissionRequest() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_FOR_LOCATION
        )
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_FOR_LOCATION) {
            permissions.forEachIndexed { index, _ ->
                if (permissions[index] == Manifest.permission.ACCESS_FINE_LOCATION
                    && grantResults[index] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun floatingButtonManager() {
        binding.floatingButton.setImageResource(R.drawable.ic_earth)
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
    }

    private fun radioGroupManager() {
        val pref = requireActivity().getSharedPreferences("radio_buttons", Context.MODE_PRIVATE)
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(-android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_enabled)
            ), intArrayOf(Color.BLACK, Color.RED)
        )

        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            val radio: RadioButton = binding.root.findViewById(checkedId)

            radio.buttonTintList = colorStateList
            when (radio) {
                okhttp -> {
                    pref.edit().putInt("pref", 1).apply()
                }
                retrofit -> {
                    pref.edit().putInt("pref", 2).apply()
                }
                loader -> {
                    pref.edit().putInt("pref", 3).apply()
                }
                room -> {
                    pref.edit().putInt("pref", 4).apply()
                }
                local -> {
                    pref.edit().putInt("pref", 0).apply()
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun renderData(appState: CityListFragmentAppState) {
        when (appState) {
            is CityListFragmentAppState.Error -> {
                binding.success()
                binding.root.snackBar(
                    resources.getString(R.string.bar_message),
                    Snackbar.LENGTH_LONG,
                    resources.getString(R.string.action_string),
                    Toast.makeText(binding.root.context, "Thank you", Toast.LENGTH_LONG)
                )
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

    private fun FragmentCityListBinding.loading() {
        fragmentLoadingLayout.visibility = View.VISIBLE
        floatingButton.visibility = View.GONE
    }

    private fun FragmentCityListBinding.success() {
        fragmentLoadingLayout.visibility = View.GONE
        floatingButton.visibility = View.VISIBLE
    }

    private fun View.snackBar(
        barMessage: String,
        duration: Int,
        actionString: String,
        lambda: Toast
    ) {
        Snackbar.make(this, barMessage, duration).setAction(actionString) { lambda.show() }.show()
    }
}


