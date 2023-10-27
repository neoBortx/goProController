package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import androidx.annotation.RequiresApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.onClosed
import kotlinx.coroutines.channels.onFailure
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.UUID


@SuppressLint("MissingPermission")
internal fun getPairedDevices(context: Context) = getBluetoothAdapter(context)?.bondedDevices ?: emptySet()

@SuppressLint("MissingPermission")
private fun getScanner(context: Context) = getBluetoothAdapter(context)?.bluetoothLeScanner

private const val SCAN_PERIOD: Long = 10000

@RequiresApi(Build.VERSION_CODES.R)
@SuppressLint("MissingPermission")
fun scanBleDevicesNearby(
    context: Context,
    serviceUuid: UUID,
    scanPeriod: Long = SCAN_PERIOD,
): Flow<BluetoothDevice> = callbackFlow {

    val leScanCallback = getLeScanCallback {
        trySendBlocking(it)
            .onFailure { fail -> fail?.printStackTrace() }
            .onClosed { fail -> fail?.printStackTrace() }
    }

    getScanner(context)?.startScan(getFilters(serviceUuid), getSettings(), leScanCallback).also {
        if (it == null) {
            close()
        }
    }

    Handler(Looper.getMainLooper()).postDelayed({
        Log.d("BleManager", "Discovering time expired")
        getScanner(context)?.stopScan(leScanCallback)
        close()
    }, scanPeriod)


    awaitClose {
        Log.d("BleManager", "Closing callback flow")
        getScanner(context)?.stopScan(leScanCallback)
        close()
    }
}

@SuppressLint("MissingPermission")
fun stopSearch(context: Context) {
    getScanner(context)?.stopScan(getLeScanCallback { })
}

private fun getSettings() = ScanSettings.Builder()
    .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
    .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
    .setMatchMode(ScanSettings.MATCH_MODE_STICKY)
    .build()

private fun getFilters(serviceUuid: UUID) = listOf(
    ScanFilter.Builder()
        .setServiceUuid(ParcelUuid(serviceUuid))
        .build()
)

private fun getLeScanCallback(onResult: (BluetoothDevice) -> Unit) = object : ScanCallback() {
    override fun onScanResult(callbackType: Int, result: android.bluetooth.le.ScanResult?) {
        super.onScanResult(callbackType, result)
        result?.device?.let {
            onResult(it)
        }
    }
}

