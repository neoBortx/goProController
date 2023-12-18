package com.bortxapps.goprocontrollerandroid.feature.connection.mapper

import android.bluetooth.BluetoothProfile
import com.bortxapps.goprocontrollerandroid.domain.data.GoProBleConnectionStatus

fun goProBleConnectionStateMapper(state: Int): GoProBleConnectionStatus {
    return when (state) {
        BluetoothProfile.STATE_DISCONNECTED -> GoProBleConnectionStatus.STATE_DISCONNECTED
        BluetoothProfile.STATE_CONNECTING -> GoProBleConnectionStatus.STATE_CONNECTING
        BluetoothProfile.STATE_CONNECTED -> GoProBleConnectionStatus.STATE_CONNECTED
        BluetoothProfile.STATE_DISCONNECTING -> GoProBleConnectionStatus.STATE_DISCONNECTING
        else -> GoProBleConnectionStatus.UNKNOWN
    }
}