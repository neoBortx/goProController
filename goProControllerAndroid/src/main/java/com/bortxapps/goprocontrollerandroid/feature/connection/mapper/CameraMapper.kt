package com.bortxapps.goprocontrollerandroid.feature.connection.mapper

import android.bluetooth.BluetoothDevice
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.domain.data.PairedState

fun BluetoothDevice.toMapCamera(): GoProCamera {
    return GoProCamera(
        name ?: "Unknown",
        address,
        bondingStateToPairedState(bondState)
    )
}

fun bondingStateToPairedState(bondingState: Int): PairedState {
    return when (bondingState) {
        BluetoothDevice.BOND_BONDED -> PairedState.PAIRED_OTHER
        BluetoothDevice.BOND_BONDING -> PairedState.PAIRING
        else -> PairedState.UNPAIRED
    }
}