package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.BleError
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.SimpleBleClientException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.BleManagerGattOperationBase
import kotlinx.coroutines.sync.Mutex
import java.util.UUID

internal class BleManagerGattReadOperations(
    private val bleManagerGattCallBacks: BleManagerGattCallBacks,
    gattMutex: Mutex,
    bleConfiguration: BleConfiguration
) :
    BleManagerGattOperationBase(gattMutex, bleConfiguration) {

    //region read data
    @SuppressLint("MissingPermission")
    suspend fun readData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        bluetoothGatt: BluetoothGatt,
        complexResponse: Boolean = false
    ): BleNetworkMessage {
        val resultRead: BleNetworkMessage?

        val characteristic = launchGattOperation {
            bluetoothGatt.getService(serviceUUID)?.getCharacteristic(characteristicUUID)
        }
        resultRead = launchGattOperation {
            bleManagerGattCallBacks.initReadOperation(complexResponse)
            if (bluetoothGatt.readCharacteristic(characteristic)) {
                launchDeferredOperation {
                    bleManagerGattCallBacks.waitForDataRead()
                }
            } else {
                null
            }
        }

        return resultRead ?: throw SimpleBleClientException(BleError.SEND_COMMAND_FAILED)
    }
    //endregion
}