package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import com.bortxapps.goprocontrollerandroid.domain.GoProError
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.BLE_DESCRIPTION_BASE_UUID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessageProcessor
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.connectToGoProBleDevice
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.getPairedDevices
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.scanBleDevicesNearby
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.stopSearch
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.UUID


class BleManager(
    private val bleNetworkMessageProcessor: BleNetworkMessageProcessor = BleNetworkMessageProcessor(),
    private val gattMutex: Mutex = Mutex()
) {

    private var bluetoothGatt: BluetoothGatt? = null

    //completions
    private var onConnectionEstablishedDeferred: CompletableDeferred<Boolean>? = null
    private var onDataReadDeferred: CompletableDeferred<BleNetworkMessage>? = null
    private var onDescriptorWriteDeferred: CompletableDeferred<Boolean>? = null
    private var onDisconnectedDeferred: CompletableDeferred<Boolean>? = null

    private var readComplexResponse: Boolean = false

    private var detectedDevices: MutableList<BluetoothDevice> = mutableListOf()

    internal fun checkPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED)
                    &&
                    (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) == PackageManager.PERMISSION_GRANTED)

        } else {
            (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH
            ) == PackageManager.PERMISSION_GRANTED)
        }
    }

    internal fun getDevicesByService(context: Context, serviceUUID: UUID): Flow<BluetoothDevice> {
        detectedDevices.clear()
        return scanBleDevicesNearby(context, serviceUUID).onEach {
            detectedDevices += it
        }
    }


    internal fun getPairedDevicesByPrefix(context: Context, deviceNamePrefix: String): List<BluetoothDevice> {
        try {
            return getPairedDevices(context).filter { it.name.startsWith(deviceNamePrefix) }
        } catch (e: SecurityException) {
            e.printStackTrace()
            throw RepositoryException(GoProError.MISSING_BLE_PERMISSIONS)
        }
    }

    internal fun stopSearchDevices(context: Context) = stopSearch(context)

    internal suspend fun connectToDevice(context: Context, address: String): Boolean =
        detectedDevices.firstOrNull { it.address == address }?.let {
            if (connect(context, it)) {
                subscribeToNotifications()
                true
            } else {
                false
            }
        } ?: false

    @OptIn(ExperimentalUnsignedTypes::class)
    private suspend fun connect(context: Context, device: BluetoothDevice): Boolean =
        withContext(Dispatchers.IO) {
            try {

                onConnectionEstablishedDeferred = CompletableDeferred()
                launchGattOperation {
                    bluetoothGatt = connectToGoProBleDevice(
                        context,
                        device,
                        onConnected = { getServices() },
                        onDisconnected = { onDisconnectedDeferred?.complete(true) },
                        onServicesDiscovered = { onConnectionEstablishedDeferred?.complete(true) },
                        onDescriptorWrite = { onDescriptorWriteDeferred?.complete(true) },
                        onCharacteristicRead = { processCharacteristic(it) },
                        onCharacteristicChanged = { _, value -> processCharacteristic(value) },
                    )
                }
                return@withContext onConnectionEstablishedDeferred!!.await()

            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext false
            }
        }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun processCharacteristic(value: UByteArray) {
        if (readComplexResponse) {
            bleNetworkMessageProcessor.processMessage(value)
        } else {
            bleNetworkMessageProcessor.processSimpleMessage(value)
        }
        if (bleNetworkMessageProcessor.isReceived()) {
            onDataReadDeferred?.complete(bleNetworkMessageProcessor.getPacket())
        }
    }

    @SuppressLint("MissingPermission")
    private fun getServices() {
        checkGatt()
        bluetoothGatt!!.discoverServices()
    }

    private suspend fun subscribeToNotifications() {
        try {
            checkGatt()
            bluetoothGatt!!.services
                ?.map { it.characteristics }
                ?.flatten()
                ?.filter { it.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0 || it.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0 }
                ?.filter { filterResponseCharacteristic(it) }
                ?.forEach { characteristic ->
                    launchGattOperation {
                        Log.d("BleManager", "subscribeToNotifications setCharacteristicNotification $characteristic")
                        val descriptorValue = if (characteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                            BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                        } else {
                            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                        }
                        writeDescriptor(characteristic, descriptorValue)
                    }
                }
        } catch (e: SecurityException) {
            e.printStackTrace()
            throw RepositoryException(GoProError.MISSING_BLE_PERMISSIONS)
        }
    }

    private fun filterResponseCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        return characteristic.uuid == GoProUUID.CQ_COMMAND_RSP.uuid
                || characteristic.uuid == GoProUUID.CQ_SETTING_RSP.uuid
                || characteristic.uuid == GoProUUID.CQ_QUERY_RSP.uuid
    }

    internal suspend fun sendData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        data: ByteArray,
        complexResponse: Boolean = false
    ): BleNetworkMessage? {
        checkGatt()
        return withContext(Dispatchers.IO) {
            onDataReadDeferred = CompletableDeferred()
            readComplexResponse = complexResponse
            if (writeCharacteristic(
                    bluetoothGatt!!,
                    serviceUUID,
                    characteristicUUID,
                    data
                )
            ) {
                onDataReadDeferred!!.await()
            } else {
                Log.e("BleManager", "sendData writeCharacteristic failed")
                null
            }
        }
    }

    private suspend fun writeCharacteristic(
        bluetoothGatt: BluetoothGatt,
        serviceUUID: UUID,
        characteristicUUID: UUID,
        value: ByteArray,
    ): Boolean {
        return try {
            val characteristic = bluetoothGatt.getService(serviceUUID)?.getCharacteristic(characteristicUUID)
            if (characteristic != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    bluetoothGatt.writeCharacteristic(
                        characteristic,
                        value,
                        BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE,
                    ) == BluetoothStatusCodes.SUCCESS
                } else {
                    bluetoothGatt.writeCharacteristic(characteristic.apply { this.value = value })
                }
            } else {
                Log.e("BleManager", "writeCharacteristic characteristic is null")
                throw RepositoryException(GoProError.SEND_COMMAND_FAILED)
            }

        } catch (e: SecurityException) {
            e.printStackTrace()
            throw RepositoryException(GoProError.MISSING_BLE_PERMISSIONS)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RepositoryException(GoProError.OTHER)
        }
    }

    internal suspend fun readData(serviceUUID: UUID, characteristicUUID: UUID, complexResponse: Boolean = false): BleNetworkMessage {
        checkGatt()
        return withContext(Dispatchers.IO) {
            onDataReadDeferred = CompletableDeferred()
            readComplexResponse = complexResponse
            readCharacteristic(bluetoothGatt!!, serviceUUID, characteristicUUID)
            onDataReadDeferred!!.await()
        }
    }

    private suspend fun readCharacteristic(
        bluetoothGatt: BluetoothGatt,
        serviceUUID: UUID,
        characteristicUUID: UUID,
    ): Boolean {
        try {
            val characteristic = launchGattOperation { bluetoothGatt.getService(serviceUUID)?.getCharacteristic(characteristicUUID) }
            return launchGattOperation { bluetoothGatt.readCharacteristic(characteristic) }
        } catch (e: SecurityException) {
            e.printStackTrace()
            throw RepositoryException(GoProError.MISSING_BLE_PERMISSIONS)
        } catch (e: Exception) {
            e.printStackTrace()
            throw RepositoryException(GoProError.OTHER)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun writeDescriptor(characteristic: BluetoothGattCharacteristic, descriptorValue: ByteArray) {
        onDescriptorWriteDeferred = CompletableDeferred()
        try {
            bluetoothGatt?.setCharacteristicNotification(characteristic, true)
            characteristic.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID))?.let { descriptor ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val res = bluetoothGatt!!.writeDescriptor(descriptor, descriptorValue)
                    if (res != BluetoothStatusCodes.SUCCESS) {
                        Log.e("BleManager", "subscribeToNotifications writeDescriptor failed -> $res")
                    } else {
                        Log.d("BleManager", "subscribeToNotifications writeDescriptor success")
                    }
                } else {
                    bluetoothGatt!!.writeDescriptor(descriptor.apply { value = descriptorValue })
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw RepositoryException(GoProError.OTHER)
        } finally {
            onDescriptorWriteDeferred!!.await()
        }

    }

    private fun checkGatt() {
        if (bluetoothGatt == null) {
            throw RepositoryException(GoProError.CAMERA_NOT_CONNECTED)
        }
    }

    private suspend fun <T> launchGattOperation(operation: suspend () -> T): T {
        return gattMutex.withLock {
            try {
                withTimeout(10000) {
                    runBlocking { operation() }
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
                throw RepositoryException(GoProError.OTHER)
            }
        }
    }
}