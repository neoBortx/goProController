package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.content.Context
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.BleManagerGattOperationBase
import kotlinx.coroutines.sync.Mutex

internal class BleManagerGattConnectionOperations(
    private val bleManagerDeviceSearchOperations: BleManagerDeviceSearchOperations,
    private val bleManagerGattCallBacks: BleManagerGattCallBacks,
    gattMutex: Mutex,
) : BleManagerGattOperationBase(gattMutex) {

    internal suspend fun connectToDevice(context: Context, address: String, gattCallBacks: BluetoothGattCallback): BluetoothGatt? {

        bleManagerDeviceSearchOperations.getDetectedDevices().firstOrNull { it.address == address }?.let {
            return connect(context, it, gattCallBacks)
        } ?: run {
            Log.e("BleManager", "connectToDevice ${GoProError.CAMERA_NOT_FOUND}")
            throw GoProException(GoProError.CAMERA_NOT_FOUND)
        }
    }


    @SuppressLint("MissingPermission")
    private suspend fun connect(
        context: Context,
        device: BluetoothDevice,
        gattCallBacks: BluetoothGattCallback
    ): BluetoothGatt? = launchGattOperation {
        bleManagerGattCallBacks.initConnectOperation()
        device.connectGatt(context, false, gattCallBacks)?.let {
            launchDeferredOperation {
                bleManagerGattCallBacks.waitForConnectionEstablished()
            }
            it
        }
    }

    @SuppressLint("MissingPermission")
    internal suspend fun disconnect(
        bluetoothGatt: BluetoothGatt,
    ): Boolean = launchGattOperation {
        bleManagerGattCallBacks.initDisconnectOperation()
        bluetoothGatt.disconnect()
        launchDeferredOperation {
            bleManagerGattCallBacks.waitForDisconnected()
        }
        true
    }


    @SuppressLint("MissingPermission")
    internal suspend fun freeConnection(
        bluetoothGatt: BluetoothGatt,
    ) = launchGattOperation {
        bluetoothGatt.close()
    }


    @SuppressLint("MissingPermission")
    internal suspend fun discoverServices(bluetoothGatt: BluetoothGatt): Boolean = launchGattOperation {
        bleManagerGattCallBacks.initDiscoverServicesOperation()
        bluetoothGatt.discoverServices()
        launchDeferredOperation {
            bleManagerGattCallBacks.waitForServicesDiscovered()
        }
    }


}