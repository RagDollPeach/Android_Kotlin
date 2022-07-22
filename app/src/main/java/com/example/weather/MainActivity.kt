package com.example.weather

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.view.contacts.ContactsFragment
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_contacts -> {
                illuminateFragment(ContactsFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //метод не дающий записывать в BackStack множество одних и тех же фрагментов
    private fun illuminateFragment(incomeFragment: Fragment?) {
        val fragments = supportFragmentManager.fragments
        var isAboutShow = false
        for (fragment in fragments) {
            if (fragment.javaClass == incomeFragment!!.javaClass && fragment.isVisible) {
                isAboutShow = true
            }
        }
        if (!isAboutShow) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, incomeFragment!!)
                .addToBackStack(null)
                .commit()
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