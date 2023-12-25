package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.contracts

import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import java.util.UUID

interface SimpleBleClientWriter {
    suspend fun sendData(serviceUUID: UUID, characteristicUUID: UUID, data: ByteArray, complexResponse: Boolean = false): BleNetworkMessage
}