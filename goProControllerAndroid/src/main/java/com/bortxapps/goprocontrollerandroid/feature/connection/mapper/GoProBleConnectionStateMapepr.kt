package com.bortxapps.goprocontrollerandroid.feature.connection.mapper

import com.bortxapps.goprocontrollerandroid.domain.data.GoProBleConnectionStatus
import com.bortxapps.simplebleclient.api.data.BleConnectionStatus

fun goProBleConnectionStateMapper(state: BleConnectionStatus): GoProBleConnectionStatus {
    return when (state) {
        BleConnectionStatus.DISCONNECTED -> GoProBleConnectionStatus.STATE_DISCONNECTED
        BleConnectionStatus.CONNECTING -> GoProBleConnectionStatus.STATE_CONNECTING
        BleConnectionStatus.CONNECTED -> GoProBleConnectionStatus.STATE_CONNECTED
        BleConnectionStatus.DISCONNECTING -> GoProBleConnectionStatus.STATE_DISCONNECTING
        else -> GoProBleConnectionStatus.UNKNOWN
    }
}