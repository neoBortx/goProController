package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.contracts

import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import java.util.UUID

interface SimpleBleClientReader {
    suspend fun readData(serviceUUID: UUID, characteristicUUID: UUID, complexResponse: Boolean = false): BleNetworkMessage
}