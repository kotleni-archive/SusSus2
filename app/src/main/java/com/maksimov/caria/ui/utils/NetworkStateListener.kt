package com.maksimov.caria.ui.utils

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.appcompat.app.AppCompatActivity

object NetworkStateListener {

    var networkState: ((Boolean) -> Unit)? = null
    var isListenNetwork = false

    fun registerListener(activity: AppCompatActivity) {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .build()

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                if (isListenNetwork) networkState?.invoke(true)
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                if (isListenNetwork) networkState?.invoke(false)
            }
        }

        val connectivityManager = activity.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
        connectivityManager.requestNetwork(networkRequest, networkCallback)
    }
}