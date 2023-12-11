package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager
import java.util.UUID

class ConnectionApi internal constructor(private val bleManager: BleManager) {
    fun getNearByCameras(serviceUUID: UUID) =
        bleManager.getDevicesByService(serviceUUID)

    fun stopSearch() = bleManager.stopSearchDevices()

    fun getPairedCameras(context: Context, deviceNamePrefix: String) =
        bleManager.getPairedDevicesByPrefix(context, deviceNamePrefix)

    suspend fun connectToDevice(context: Context, address: String) =
        bleManager.connectToDevice(context, address)
}