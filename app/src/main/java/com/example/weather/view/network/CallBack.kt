package com.example.weather.view.network

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.weather.R
import com.example.weather.utils.NETWORK_ACTION
import com.example.weather.utils.NETWORK_KEY

class CallBack(private val context: Context) : ConnectivityManager.NetworkCallback() {

    override fun onLost(network: Network) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent().apply {
            putExtra(NETWORK_KEY, context.resources.getString(R.string.network_lost))
            action = NETWORK_ACTION
        })
    }

    override fun onAvailable(network: Network) {
        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent().apply {
            putExtra(NETWORK_KEY, context.resources.getString(R.string.network_is_available))
            action = NETWORK_ACTION
        })
    }
}