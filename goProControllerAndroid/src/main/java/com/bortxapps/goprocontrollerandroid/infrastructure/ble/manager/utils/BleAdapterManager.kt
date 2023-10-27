package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService

fun getBluetoothAdapter(context: Context) =
    getSystemService(context, BluetoothManager::class.java)?.adapter