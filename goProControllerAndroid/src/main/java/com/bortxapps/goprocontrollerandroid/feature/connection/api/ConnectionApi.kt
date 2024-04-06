package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.annotation.SuppressLint
import android.content.Context
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.simplebleclient.api.contracts.SimpleBleClient
import java.util.UUID

class ConnectionApi internal constructor(private val bleClient: SimpleBleClient) {
    suspend fun getNearByCameras(serviceUUID: UUID) =
        bleClient.deviceSeeker.getDevicesNearby(serviceUUID = serviceUUID)

    suspend fun stopSearch() = bleClient.deviceSeeker.stopSearchDevices()

    @SuppressLint("MissingPermission")
    suspend fun getPairedCameras(context: Context, deviceNamePrefix: String) =
        bleClient.deviceSeeker.getPairedDevices(context).filter { it.name.startsWith(deviceNamePrefix) }

    suspend fun connectToDevice(context: Context, address: String) =
        bleClient.connection.connectToDevice(context, address).also {
            if (it) {
                bleClient.subscription.subscribeToCharacteristicChanges(
                    listOf(
                        GoProUUID.CQ_COMMAND_RSP.uuid,
                        GoProUUID.CQ_SETTING_RSP.uuid,
                        GoProUUID.CQ_QUERY_RSP.uuid
                    )
                )
            }
        }

    fun subscribeToConnectionStatusChanges() = bleClient.connection.subscribeToConnectionStatusChanges()

    suspend fun disconnectBle() = bleClient.connection.disconnect()
}