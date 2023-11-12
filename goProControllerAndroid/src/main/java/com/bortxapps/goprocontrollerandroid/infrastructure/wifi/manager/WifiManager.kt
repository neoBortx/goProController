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
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn

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
    ): Flow<Result<WifiStatus>> = callbackFlow {
        val connectivityManager by lazy { context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager }

        val wifiNetworkSpecifier =
            WifiNetworkSpecifier.Builder().setSsid(ssid).setWpa2Passphrase(password).build()

        val networkRequest =
            NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .setNetworkSpecifier(wifiNetworkSpecifier).build()

        trySend(Result.success(WifiStatus.CONNECTING))

        Log.d("WifiManager", "connectToWifi connecting $ssid $password")

        try {
            connectivityManager.requestNetwork(
                networkRequest,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        super.onAvailable(network)
                        Log.d("WifiManager", "connectToWifi onAvailable")
                        connectivityManager.bindProcessToNetwork(network)
                        trySend(Result.success(WifiStatus.CONNECTED))
                    }

                    override fun onLost(network: Network) {
                        super.onLost(network)
                        Log.d("WifiManager", "connectToWifi onLost")
                        connectivityManager.bindProcessToNetwork(null)
                        connectivityManager.unregisterNetworkCallback(this)
                        trySend(Result.success(WifiStatus.CONNECTION_LOST))
                        close()
                    }

                    override fun onUnavailable() {
                        super.onUnavailable()
                        Log.d("WifiManager", "connectToWifi onUnavailable")
                        connectivityManager.bindProcessToNetwork(null)
                        connectivityManager.unregisterNetworkCallback(this)
                        trySend(Result.success(WifiStatus.CONNECTION_FAILED))
                        close()
                    }
                }
            )
        }catch (e: Exception){
            Log.d("WifiManager", "connectToWifi Erro ${e.message} -> ${e.stackTraceToString()}")
            trySend(Result.failure(GoProException(GoProError.UNABLE_TO_CONNECT_TO_WIFI)))
            close()
        }

        awaitClose {
            Log.d("WifiManager", "connectToWifi closing")
            close()
        }
    }.flowOn(Dispatchers.IO)
}