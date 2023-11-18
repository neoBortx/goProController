package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.ParcelUuid
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.util.UUID

class BleDeviceScanner {

    companion object {
        private const val SCAN_PERIOD: Long = 10000
    }

    @SuppressLint("MissingPermission")
    fun scanBleDevicesNearby(
        context: Context, serviceUuid: UUID, scanPeriod: Long = SCAN_PERIOD
    ): Flow<BluetoothDevice> = callbackFlow {
        val scanner = getBluetoothAdapter(context)?.bluetoothLeScanner
        val leScanCallback = getLeScanCallback(onResult = {
            trySendBlocking(it)
        }, onFailure = {
            close(GoProException(GoProError.CANNOT_START_SEARCHING_CAMERAS))
        })

        try {
            scanner?.startScan(getFilters(serviceUuid), getSettings(), leScanCallback).also {
                if (it == null) {
                    close(GoProException(GoProError.CANNOT_START_SEARCHING_CAMERAS))
                }
            }
        } catch (ex: Exception) {
            Log.e("BleManager", "Error starting scan ${ex.message} ${ex.stackTraceToString()}")
            close(GoProException(GoProError.CANNOT_START_SEARCHING_CAMERAS))
        }


        Handler(Looper.getMainLooper()).postDelayed({
            try {
                Log.d("BleManager", "Discovering time expired")
                scanner?.stopScan(leScanCallback)
            } catch (ex: Exception) {
                Log.e("BleManager", "Error stopping scan ${ex.message} ${ex.stackTraceToString()}")
            }

            close()
        }, scanPeriod)

        awaitClose {
            try {
                Log.d("BleManager", "Closing callback flow")
                scanner?.stopScan(leScanCallback)
            } catch (ex: Exception) {
                Log.e("BleManager", "Error stopping scan ${ex.message} ${ex.stackTraceToString()}")
            }
            close()
        }
    }.flowOn(Dispatchers.IO)


    @SuppressLint("MissingPermission")
    fun stopSearch(context: Context) {
        getBluetoothAdapter(context)?.bluetoothLeScanner?.stopScan(getLeScanCallback({ }, {}))
    }

    private fun getSettings() =
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
            .setMatchMode(ScanSettings.MATCH_MODE_STICKY).build()

    private fun getFilters(serviceUuid: UUID) = listOf(
        ScanFilter.Builder().setServiceUuid(ParcelUuid(serviceUuid)).build()
    )

    private fun getLeScanCallback(onResult: (BluetoothDevice) -> Unit,
                                  onFailure: () -> Unit) = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: android.bluetooth.le.ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.device?.let {
                onResult(it)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            results?.forEach {result ->
                result.device?.let {
                    onResult(it)
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
            onFailure()
        }


    }
}