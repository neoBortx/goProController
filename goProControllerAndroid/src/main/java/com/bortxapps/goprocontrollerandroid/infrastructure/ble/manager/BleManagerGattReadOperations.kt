package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.BleManagerGattOperationBase
import kotlinx.coroutines.sync.Mutex
import java.util.UUID

internal class BleManagerGattReadOperations(private val bleManagerGattCallBacks: BleManagerGattCallBacks, gattMutex: Mutex) :
    BleManagerGattOperationBase(gattMutex) {

    //region read data
    @SuppressLint("MissingPermission")
    internal suspend fun readData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        bluetoothGatt: BluetoothGatt,
        complexResponse: Boolean = false
    ): BleNetworkMessage {
        var resultRead: BleNetworkMessage? = null

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

        return resultRead ?: throw GoProException(GoProError.SEND_COMMAND_FAILED)
    }
    //endregion
}