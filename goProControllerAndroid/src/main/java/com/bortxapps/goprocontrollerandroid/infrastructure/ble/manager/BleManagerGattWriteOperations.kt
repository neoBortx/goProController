package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import android.util.Log
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.BleError
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.SimpleBleClientException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.BleManagerGattOperationBase
import com.bortxapps.goprocontrollerandroid.utils.BuildVersionProvider
import kotlinx.coroutines.sync.Mutex
import java.util.UUID

internal class BleManagerGattWriteOperations(
    private val bleManagerGattCallBacks: BleManagerGattCallBacks,
    private val buildVersionProvider: BuildVersionProvider,
    gattMutex: Mutex,
    bleConfiguration: BleConfiguration
) : BleManagerGattOperationBase(gattMutex, bleConfiguration) {

    //region send data
    suspend fun sendData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        data: ByteArray,
        bluetoothGatt: BluetoothGatt,
        complexResponse: Boolean
    ): BleNetworkMessage {
        bluetoothGatt.getService(serviceUUID)?.getCharacteristic(characteristicUUID)?.let {
            return writeCharacteristic(
                bluetoothGatt,
                data,
                it,
                complexResponse
            )
        } ?: run {
            Log.e("BleManager", "writeCharacteristic characteristic is null")
            throw SimpleBleClientException(BleError.SEND_COMMAND_FAILED)
        }

    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "NewApi")
    private suspend fun writeCharacteristic(
        bluetoothGatt: BluetoothGatt,
        value: ByteArray,
        characteristic: BluetoothGattCharacteristic,
        complexResponse: Boolean
    ): BleNetworkMessage {
        return launchGattOperation {
            bleManagerGattCallBacks.initReadOperation(complexResponse)
            val res = if (buildVersionProvider.getSdkVersion() >= Build.VERSION_CODES.TIRAMISU) {
                bluetoothGatt.writeCharacteristic(
                    characteristic,
                    value,
                    BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                ) == BluetoothStatusCodes.SUCCESS

            } else {
                bluetoothGatt.writeCharacteristic(characteristic.apply { this.value = value })
            }

            if (res) {
                launchDeferredOperation {
                    bleManagerGattCallBacks.waitForDataRead()
                }

            } else {
                null
            }
        } ?: run {
            throw SimpleBleClientException(BleError.SEND_COMMAND_FAILED)
        }
    }
    //endregion
}