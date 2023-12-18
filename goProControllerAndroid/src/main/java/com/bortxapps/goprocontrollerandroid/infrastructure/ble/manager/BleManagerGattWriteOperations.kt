package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.BleManagerGattOperationBase
import com.bortxapps.goprocontrollerandroid.urils.BuildVersionProvider
import kotlinx.coroutines.sync.Mutex
import java.util.UUID

internal class BleManagerGattWriteOperations(
    private val bleManagerGattCallBacks: BleManagerGattCallBacks,
    private val buildVersionProvider: BuildVersionProvider,
    gattMutex: Mutex,
) : BleManagerGattOperationBase(gattMutex) {

    //region send data
    internal suspend fun sendData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        data: ByteArray,
        bluetoothGatt: BluetoothGatt,
        complexResponse: Boolean = false
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
            throw GoProException(GoProError.SEND_COMMAND_FAILED)
        }

    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "NewApi")
    private suspend fun writeCharacteristic(
        bluetoothGatt: BluetoothGatt,
        value: ByteArray,
        characteristic: BluetoothGattCharacteristic,
        complexResponse: Boolean = false
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
                bleManagerGattCallBacks.waitForDataRead()
            } else {
                null
            }
        } ?: run {
            throw GoProException(GoProError.SEND_COMMAND_FAILED)
        }
    }
    //endregion
}