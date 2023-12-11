package com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.BluetoothLeScanner
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import java.util.Timer
import java.util.UUID
import kotlin.concurrent.schedule

internal class BleDeviceScannerManager(
    private val scanner: BluetoothLeScanner,
    private val bleDeviceScannerSettingsBuilder: BleDeviceScannerSettingsBuilder,
    private val bleDeviceScannerFilterBuilder: BleDeviceScannerFilterBuilder,
    private val bleDeviceScannerCallbackBuilder: BleDeviceScannerCallbackBuilder,
    private val bleScannerTimerHandler: Timer = Timer()
) {

    companion object {
        private const val SCAN_PERIOD: Long = 10000
    }

    private var onStopSearch: () -> Unit = {}
    private var stopSearchObservable = true
        set(value) {
            onStopSearch()
            field = value
        }

    @SuppressLint("MissingPermission")
    fun scanBleDevicesNearby(serviceUuid: UUID, scanPeriod: Long = SCAN_PERIOD): Flow<BluetoothDevice> = callbackFlow {
        onStopSearch = {
            Log.d("BleManager", "stopSearchObserver")
            close()
        }


        val leScanCallback = configureScanCallback(onResult = {
            trySendBlocking(it)
        }, onFailure = {
            close(GoProException(GoProError.CANNOT_START_SEARCHING_CAMERAS))
        })

        try {
            scanner.startScan(
                bleDeviceScannerFilterBuilder.buildFilters(serviceUuid),
                bleDeviceScannerSettingsBuilder.buildScanSettings(),
                leScanCallback
            )

        } catch (ex: Exception) {
            Log.e("BleManager", "Error starting scan ${ex.message} ${ex.stackTraceToString()}")
            close(GoProException(GoProError.CANNOT_START_SEARCHING_CAMERAS))
        }

        bleScannerTimerHandler.schedule(scanPeriod) {
            Log.d("BleManager", "Discovering time expired")
            close()
        }


        awaitClose {
            Log.d("BleManager", "awaitClose")
            scanner.stopScan(leScanCallback)
            bleScannerTimerHandler.cancel()
            close()
        }
    }.flowOn(Dispatchers.IO)

    fun stopSearch() {
        Log.d("BleManager", "stopSearch")
        stopSearchObservable = true
    }

    private fun configureScanCallback(
        onResult: (BluetoothDevice) -> Unit,
        onFailure: () -> Unit
    ) = bleDeviceScannerCallbackBuilder.buildScanCallback(onResult, onFailure)
}