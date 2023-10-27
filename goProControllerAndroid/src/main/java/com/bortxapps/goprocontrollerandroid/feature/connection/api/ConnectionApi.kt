package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager
import java.util.UUID

class ConnectionApi(val bleManager: BleManager = BleManager()) {
    fun getNearByCameras(context: Context, serviceUUID: UUID) =
        bleManager.getDevicesByService(context, serviceUUID)

    fun stopSearch(context: Context) = bleManager.stopSearchDevices(context)

    fun getPairedCameras(context: Context, deviceNamePrefix: String) =
        bleManager.getPairedDevicesByPrefix(context, deviceNamePrefix)

    fun checkPermissions(context: Context) =
        bleManager.checkPermissions(context)

    suspend fun connectToDevice(context: Context, address: String) =
        bleManager.connectToDevice(context, address)

    suspend fun getWifiApSSID() =
        bleManager.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_SSID.uuid)

    suspend fun getWifiApPassword() =
        bleManager.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_PASSWORD.uuid)

    suspend fun enableWifiAp() =
        bleManager.sendData(
            GoProUUID.WIFI_AP_SERVICE.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.EnableWifiCommand.byteArray
        )

    suspend fun disableWifiAp() =
        bleManager.sendData(
            GoProUUID.WIFI_AP_SERVICE.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.DisableWifiCommand.byteArray,
        )

    suspend fun setPresetsVideo() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsVideoCommand.byteArray,
        )

    suspend fun setPresetsPhoto() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsPhotoCommand.byteArray,
        )

    suspend fun setPresetsTimeLapse() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsTimeLapseCommand.byteArray,
        )

    suspend fun getOpenGoProVersion() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_COMMAND.uuid,
        GoProBleCommands.GetOpenGoProVersionCommand.byteArray
    )

    suspend fun getHardwareInfo() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_COMMAND.uuid,
        GoProBleCommands.GetHardwareInfoCommand.byteArray,
        complexResponse = true
    )

    suspend fun getCameraSettings() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetCameraSettings.byteArray,
        complexResponse = true
    )

    suspend fun getCameraStatus() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetCameraStatus.byteArray,
        complexResponse = true
    )
}