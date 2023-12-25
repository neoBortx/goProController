package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.contracts

import android.bluetooth.BluetoothDevice
import android.content.Context
import kotlinx.coroutines.flow.Flow
import java.util.UUID

interface SimpleBleClientDeviceSeeker {
    fun getDevicesByService(serviceUUID: UUID): Flow<BluetoothDevice>
    fun getPairedDevicesByPrefix(context: Context, deviceNamePrefix: String): List<BluetoothDevice>
    fun stopSearchDevices()
}