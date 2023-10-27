package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGatt.GATT_SUCCESS
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothProfile
import android.content.Context
import android.util.Log


@OptIn(ExperimentalUnsignedTypes::class)
private fun bluetoothGattCallback(
    onConnected: () -> Unit,
    onDisconnected: () -> Unit,
    onCharacteristicRead: (UByteArray) -> Unit,
    onCharacteristicChanged: (BluetoothGattCharacteristic?, UByteArray) -> Unit,
    onServicesDiscovered: () -> Unit,
    onDescriptorWrite: () -> Unit
) =
    object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    Log.d("BleConnectionManager", "onConnectionStateChange: STATE_CONNECTED")
                    onConnected()
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    Log.d("BleConnectionManager", "onConnectionStateChange: STATE_DISCONNECTED")
                    onDisconnected()
                }
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            if (status == GATT_SUCCESS) {
                Log.d("BleConnectionManager", "onCharacteristicRead: ${characteristic.uuid}} value ${value.toUByteArray()}")
                onCharacteristicRead(value.toUByteArray())
            } else {
                Log.d("BleConnectionManager", "onCharacteristicRead: ${characteristic.uuid} FAIL")
                Throwable(status.toString())
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, value: ByteArray) {
            Log.d(
                "BleConnectionManager",
                "onCharacteristicChanged: ${characteristic.uuid}} value ${value.joinToString(separator = ":") { String.format("%02X", it) }}"
            )
            onCharacteristicChanged(characteristic, value.toUByteArray())
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            if (status == GATT_SUCCESS) {
                Log.d("BleConnectionManager", "onServicesDiscovered: ${gatt.services}")
                onServicesDiscovered()
            } else {
                Log.d("BleConnectionManager", "onServicesDiscovered: FAIL")
                Throwable(status.toString())
            }
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            if (status == GATT_SUCCESS) {
                Log.d("BleConnectionManager", "onDescriptorWrite: ${descriptor?.uuid}")
                onDescriptorWrite()
            } else {
                Log.d("BleConnectionManager", "onDescriptorWrite:  FAIL")
                Throwable(status.toString())
            }
        }
    }

@OptIn(ExperimentalUnsignedTypes::class)
@SuppressLint("MissingPermission")
fun connectToGoProBleDevice(
    context: Context,
    device: BluetoothDevice,
    onConnected: () -> Unit,
    onDisconnected: () -> Unit,
    onCharacteristicRead: (UByteArray) -> Unit,
    onCharacteristicChanged: (BluetoothGattCharacteristic?, UByteArray) -> Unit,
    onServicesDiscovered: () -> Unit,
    onDescriptorWrite: () -> Unit
): BluetoothGatt = device.connectGatt(
    context,
    false,
    bluetoothGattCallback(
        onConnected,
        onDisconnected,
        onCharacteristicRead,
        onCharacteristicChanged,
        onServicesDiscovered,
        onDescriptorWrite
    )
)