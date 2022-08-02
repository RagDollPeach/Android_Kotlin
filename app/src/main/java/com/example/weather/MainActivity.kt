package com.example.weather

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.example.weather.databinding.ActivityMainBinding
import com.example.weather.utils.HIGH_CHANNEL_ID
import com.example.weather.utils.NOTIFICATION_CHANNEL_NAME
import com.example.weather.utils.NOTIFICATION_ID
import com.example.weather.view.contacts.ContactsFragment
import com.example.weather.view.maps.MapsFragment
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

      //  pushNotification("Бизнес треннинг", "Новая лекция , шанс заработать моллион за 5 минут, лекцию ведет COUCH - бомж Порфилий")
    }

    private fun pushNotification(title: String, body: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(this, HIGH_CHANNEL_ID).apply {
            setContentTitle(title)
            setContentText(body)
            setContentIntent(pendingIntent)
            setSmallIcon(R.drawable.ic_kotlin_logo)
            setAutoCancel(true)
            priority = NotificationCompat.PRIORITY_MAX
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelHigh = NotificationChannel(HIGH_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            channelHigh.description = "Понял зачем это ненужное - нужно"
            notificationManager.createNotificationChannel(channelHigh)
        }
        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_contacts -> {
                startFragment(ContactsFragment())
                true
            }

            R.id.menu_maps -> {
                startFragment(MapsFragment())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    // оставляю закоментированый код, что бы не забыть, что есть и такой вариант, как пример
    private fun startFragment(incomeFragment: Fragment?) {
        val fragment = supportFragmentManager.findFragmentByTag("tag")

        if (fragment == null) {
            supportFragmentManager.apply {
                beginTransaction()
                    .replace(R.id.container, incomeFragment!!, "tag")
                    .addToBackStack("")
                    .commit()
            }
        }
     /* val fragments = supportFragmentManager.fragments
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
        }*/
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