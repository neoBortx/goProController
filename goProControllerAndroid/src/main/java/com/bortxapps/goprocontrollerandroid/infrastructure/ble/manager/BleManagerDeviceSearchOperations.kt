package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner.BleDeviceScannerManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import java.util.UUID

internal class BleManagerDeviceSearchOperations(
    private val bleScanner: BleDeviceScannerManager,
) {

    private var searchingDevices = false
    private val detectedDevices = mutableListOf<BluetoothDevice>()
    internal fun getDevicesByService(serviceUUID: UUID): Flow<BluetoothDevice> {
        if (!searchingDevices) {
            searchingDevices = true
            detectedDevices.clear()
            return bleScanner.scanBleDevicesNearby(serviceUUID).onEach {
                detectedDevices += it
            }.onCompletion {
                searchingDevices = false
            }
        } else {
            Log.e("BleManager", "getDevicesByService already searching devices")
            throw GoProException(GoProError.ALREADY_SEARCHING_CAMERAS)
        }
    }

    @SuppressLint("MissingPermission")
    internal fun getPairedDevicesByPrefix(context: Context, deviceNamePrefix: String): List<BluetoothDevice> =
        context.getSystemService(BluetoothManager::class.java)
            ?.adapter
            ?.bondedDevices
            ?.filter { it.name.startsWith(deviceNamePrefix) }
            .orEmpty()


    internal fun getDetectedDevices() = detectedDevices.toList()

    internal fun stopSearchDevices() = bleScanner.stopSearch()
}