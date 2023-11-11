package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.provider.Settings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class WifiManager {

    fun enableWifi(context: Context) {
        val wifiManager: WifiManager by lazy { context.getSystemService(Context.WIFI_SERVICE) as WifiManager }

        if (!wifiManager.isWifiEnabled) {
            val panelIntent = Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY)
            panelIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(panelIntent)
        }
    }

    internal fun connectToWifi(
        ssid: String,
        password: String,
        context: Context
    ): Flow<Boolean> = callbackFlow {
        val connectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

        val wifiNetworkSpecifier =
            WifiNetworkSpecifier.Builder().setSsid(ssid).setWpa2Passphrase(password).build()

        val networkRequest =
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .setNetworkSpecifier(wifiNetworkSpecifier).build()

        connectivityManager.requestNetwork(
            networkRequest,
            object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    connectivityManager.bindProcessToNetwork(network)
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    // Just in case
                    connectivityManager.bindProcessToNetwork(null)
                    connectivityManager.unregisterNetworkCallback(this)
                    trySend(false)
                }

                override fun onUnavailable() {
                    trySend(false)
                }
            }
        )
    }
}