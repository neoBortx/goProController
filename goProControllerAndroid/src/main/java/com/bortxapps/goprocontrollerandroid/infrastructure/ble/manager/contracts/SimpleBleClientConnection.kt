package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.contracts

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow

interface SimpleBleClientConnection {
    suspend fun connectToDevice(context: Context, address: String): Boolean
    suspend fun disconnect()
    fun subscribeToConnectionStatusChanges(): MutableStateFlow<Int>

}