package com.nmc.myapplication.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkRequest

/**
 * true, if device is online,
 * else false.
 */
fun Context.isConnectionAvailable(): Boolean {
    val connectivityManager =
        this.getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    return connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
}

/**
 * Inform when device comes online.
 */
fun Context.trackNetworkConnection(networkCallback : ConnectivityManager.NetworkCallback) {
    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    val connectivityManager = getSystemService(ConnectivityManager::class.java) as ConnectivityManager
    connectivityManager.requestNetwork(networkRequest, networkCallback)
}