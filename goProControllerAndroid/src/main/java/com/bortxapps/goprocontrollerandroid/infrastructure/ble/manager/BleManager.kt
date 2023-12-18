package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.bluetooth.BluetoothGatt
import android.content.Context
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID

internal class BleManager(
    private val bleManagerDeviceConnection: BleManagerDeviceSearchOperations,
    private val bleManagerGattConnectionOperations: BleManagerGattConnectionOperations,
    private val bleManagerGattSubscriptions: BleManagerGattSubscriptions,
    private val bleManagerGattReadOperations: BleManagerGattReadOperations,
    private val bleManagerGattWriteOperations: BleManagerGattWriteOperations,
    private val bleManagerGattCallBacks: BleManagerGattCallBacks
) {

    private var bluetoothGatt: BluetoothGatt? = null

    init {
        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch(IO) {
            bleManagerGattCallBacks.subscribeToConnectionStatusChanges().collect {
                if (it == BluetoothGatt.STATE_DISCONNECTED) {
                    freeResources()
                }
            }
        }
    }

    //region search devices
    fun getDevicesByService(serviceUUID: UUID) =
        bleManagerDeviceConnection.getDevicesByService(serviceUUID)

    fun getPairedDevicesByPrefix(context: Context, deviceNamePrefix: String) =
        bleManagerDeviceConnection.getPairedDevicesByPrefix(context, deviceNamePrefix)

    fun stopSearchDevices() = bleManagerDeviceConnection.stopSearchDevices()
    //endregion

    //region connection
    suspend fun connectToDevice(context: Context, address: String): Boolean {
        bleManagerGattConnectionOperations.connectToDevice(context, address, bleManagerGattCallBacks)?.let {
            bluetoothGatt = it
            if (bleManagerGattConnectionOperations.discoverServices(it)) {
                bleManagerGattSubscriptions.subscribeToNotifications(it)
            }
        } ?: throw GoProException(GoProError.CAMERA_NOT_CONNECTED)
        return true
    }

    internal suspend fun disconnect() {
        checkGatt()
        if (bleManagerGattConnectionOperations.disconnect(bluetoothGatt!!)) {
            freeResources()
        }
    }

    private suspend fun freeResources() {
        bluetoothGatt?.let {
            bleManagerGattConnectionOperations.freeConnection(it)
            bleManagerGattCallBacks.reset()
            bluetoothGatt = null
        }
    }

    internal fun subscribeToConnectionStatusChanges() = bleManagerGattCallBacks.subscribeToConnectionStatusChanges()
    //endregion

    //region IO
    suspend fun sendData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        data: ByteArray,
        complexResponse: Boolean = false
    ): BleNetworkMessage {
        checkGatt()
        return bleManagerGattWriteOperations.sendData(
            serviceUUID,
            characteristicUUID,
            data,
            bluetoothGatt!!,
            complexResponse,
        )
    }

    suspend fun readData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        complexResponse: Boolean = false
    ): BleNetworkMessage {
        checkGatt()
        return bleManagerGattReadOperations.readData(
            serviceUUID,
            characteristicUUID,
            bluetoothGatt!!,
            complexResponse
        )
    }
    //endregion

    //region private methods
    private fun checkGatt() {
        if (bluetoothGatt == null) {
            throw GoProException(GoProError.CAMERA_NOT_CONNECTED)
        }
    }

//endregion
}