package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.simplebleclient.manager.contracts.SimpleBleClient
import java.util.UUID

class ConnectionApi internal constructor(private val bleClient: SimpleBleClient) {
    suspend fun getNearByCameras(serviceUUID: UUID) =
        bleClient.getDevicesByService(serviceUUID)

    suspend fun stopSearch() = bleClient.stopSearchDevices()

    suspend fun getPairedCameras(context: Context, deviceNamePrefix: String) =
        bleClient.getPairedDevicesByPrefix(context, deviceNamePrefix)

    suspend fun connectToDevice(context: Context, address: String) =
        bleClient.connectToDevice(context, address).also {
            if (it) {
                bleClient.subscribeToCharacteristicChanges(
                    listOf(
                        GoProUUID.CQ_COMMAND_RSP.uuid,
                        GoProUUID.CQ_SETTING_RSP.uuid,
                        GoProUUID.CQ_QUERY_RSP.uuid
                    )
                )
            }
        }

    fun subscribeToConnectionStatusChanges() = bleClient.subscribeToConnectionStatusChanges()

    suspend fun disconnectBle() = bleClient.disconnect()
}