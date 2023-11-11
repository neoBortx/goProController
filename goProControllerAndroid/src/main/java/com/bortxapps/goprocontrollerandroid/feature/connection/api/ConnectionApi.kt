package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager
import java.util.UUID

class ConnectionApi(val bleManager: BleManager = BleManager.instance) {
    fun getNearByCameras(context: Context, serviceUUID: UUID) =
        bleManager.getDevicesByService(context, serviceUUID)

    fun stopSearch(context: Context) = bleManager.stopSearchDevices(context)

    fun getPairedCameras(context: Context, deviceNamePrefix: String) =
        bleManager.getPairedDevicesByPrefix(context, deviceNamePrefix)

    suspend fun connectToDevice(context: Context, address: String) =
        bleManager.connectToDevice(context, address)
}