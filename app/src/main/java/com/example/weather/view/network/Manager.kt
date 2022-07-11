package com.example.weather.view.network

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class Manager(context: Context) {

    private val callBack: CallBack = CallBack(context)

    private val connectivityManager =
        context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

    fun registerCallBack() {
        val request = wifiRequest()
        connectivityManager.registerNetworkCallback(request, callBack)
    }

    fun unregisterCallBack() {
        connectivityManager.unregisterNetworkCallback(callBack)
    }

    private fun wifiRequest(): NetworkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_RESTRICTED)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .build()
}
