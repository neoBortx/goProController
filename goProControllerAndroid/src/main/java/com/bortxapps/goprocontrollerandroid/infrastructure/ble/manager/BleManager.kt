package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.bluetooth.BluetoothGatt
import android.content.Context
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.BleError
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.SimpleBleClientException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.contracts.SimpleBleClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.util.UUID

internal class BleManager (
    private val bleManagerDeviceConnection: BleManagerDeviceSearchOperations,
    private val bleManagerGattConnectionOperations: BleManagerGattConnectionOperations,
    private val bleManagerGattSubscriptions: BleManagerGattSubscriptions,
    private val bleManagerGattReadOperations: BleManagerGattReadOperations,
    private val bleManagerGattWriteOperations: BleManagerGattWriteOperations,
    private val bleManagerGattCallBacks: BleManagerGattCallBacks,
) : SimpleBleClient {

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
    override fun getDevicesByService(serviceUUID: UUID) =
        bleManagerDeviceConnection.getDevicesByService(serviceUUID)

    override fun getPairedDevicesByPrefix(context: Context, deviceNamePrefix: String) =
        bleManagerDeviceConnection.getPairedDevicesByPrefix(context, deviceNamePrefix)

    override fun stopSearchDevices() = bleManagerDeviceConnection.stopSearchDevices()
    //endregion

    //region connection
    override suspend fun connectToDevice(context: Context, address: String): Boolean {
        bleManagerGattConnectionOperations.connectToDevice(context, address, bleManagerGattCallBacks)?.let {
            bluetoothGatt = it
            if (bleManagerGattConnectionOperations.discoverServices(it)) {
                bleManagerGattSubscriptions.subscribeToNotifications(it)
            }
        } ?: throw SimpleBleClientException(BleError.CAMERA_NOT_CONNECTED)
        return true
    }

    override suspend fun disconnect() {
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

    override fun subscribeToConnectionStatusChanges() = bleManagerGattCallBacks.subscribeToConnectionStatusChanges()
    //endregion

    //region IO
    override suspend fun sendData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        data: ByteArray,
        complexResponse: Boolean
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

    override suspend fun readData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        complexResponse: Boolean
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
            throw SimpleBleClientException(BleError.CAMERA_NOT_CONNECTED)
        }
    }

//endregion
}