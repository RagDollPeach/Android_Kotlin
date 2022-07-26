package com.example.weather.view.maps

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.weather.R
import com.example.weather.databinding.MapsUiBinding
import com.example.weather.utils.REQUEST_CODE_FOR_LOCATION
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import java.io.IOException

class MapsFragment : Fragment() {

    private var _binding: MapsUiBinding? = null
    private val binding: MapsUiBinding
        get() {
            return _binding!!
        }

    private lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->
        map = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap.setOnMapLongClickListener { latLng ->
            addMarkerToArray(latLng)
            setMarker(latLng, "", R.drawable.ic_map_pin)
            drawLine()
        }

        googleMap.uiSettings.isZoomControlsEnabled = true

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //прописал проверку, а она все равно крассным подчеркнута
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        } else {
            checkPermission()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MapsUiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        // получилось прописать проверку, приложение больше не падает
        binding.buttonSearch.setOnClickListener {
            binding.searchAddress.text.toString().let { searchText ->
               searchMaps(searchText)
            }
        }
    }
    // написал одинаковые тосты потому, что все эти исключения выбрвсываются из за плохо заполненого поля поиска
    private fun searchMaps(searchText: String) {
        val geocoder = Geocoder(requireContext())
        Thread {
            try {
                val result = geocoder.getFromLocationName(searchText, 1)
                val ln = LatLng(result.first().latitude, result.first().longitude)
                requireActivity().runOnUiThread {
                    setMarker(ln, searchText, R.drawable.ic_map_marker)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(ln, 8f))
                }
            } catch (e: RuntimeException) {
                e.printStackTrace()
                Looper.prepare().let { Toast.makeText(requireContext(), R.string.searching_maps_message, Toast.LENGTH_LONG).show()  }
            } catch (e: IOException) {
                e.printStackTrace()
                Looper.prepare().let { Toast.makeText(requireContext(), R.string.searching_maps_message, Toast.LENGTH_LONG).show()  }
            } catch (e: NoSuchElementException) {
                e.printStackTrace()
                Looper.prepare().let { Toast.makeText(requireContext(), R.string.searching_maps_message, Toast.LENGTH_LONG).show()  }
            }
        }.start()
    }

    private fun checkPermission() {
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            map.isMyLocationEnabled = true
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
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_CODE_FOR_LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_FOR_LOCATION) {
            for (pIndex in permissions.indices) {
                    if (ActivityCompat.checkSelfPermission(requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        map.isMyLocationEnabled = true
                    }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private val markers = mutableListOf<Marker>()
    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(15f)
            )
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourceId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourceId)))!!
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}