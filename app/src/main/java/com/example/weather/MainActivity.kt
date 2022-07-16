package com.example.weather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.view.network.Manager
import com.example.weather.view.weatherlist.CitiesListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val networkManager by lazy { Manager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container,
                CitiesListFragment.getInstance()
            ).commit()
        }


    }

    override fun onStart() {
        super.onStart()
        networkManager.registerCallBack()
    }

    override fun onStop() {
        super.onStop()
        networkManager.unregisterCallBack()
    }
}