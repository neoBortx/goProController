package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.contracts.SimpleBleClient
import java.util.UUID

class ConnectionApi internal constructor(private val bleClient: SimpleBleClient) {
    fun getNearByCameras(serviceUUID: UUID) =
        bleClient.getDevicesByService(serviceUUID)

    fun stopSearch() = bleClient.stopSearchDevices()

    fun getPairedCameras(context: Context, deviceNamePrefix: String) =
        bleClient.getPairedDevicesByPrefix(context, deviceNamePrefix)

    suspend fun connectToDevice(context: Context, address: String) =
        bleClient.connectToDevice(context, address)

    fun subscribeToConnectionStatusChanges() = bleClient.subscribeToConnectionStatusChanges()

    suspend fun disconnectBle() = bleClient.disconnect()
}