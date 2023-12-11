package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothStatusCodes
import android.content.Context
import android.os.Build
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.BLE_DESCRIPTION_BASE_UUID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessageProcessor
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.connectToGoProBleDevice
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner.BleDeviceScannerManager
import com.bortxapps.goprocontrollerandroid.urils.BuildVersionProvider
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import java.util.UUID

internal class BleManager(
    private val bleNetworkMessageProcessor: BleNetworkMessageProcessor,
    private val bleScanner: BleDeviceScannerManager,
    private val gattMutex: Mutex,
    private val buildVersionProvider: BuildVersionProvider
) {

    companion object {
        private const val MUTEX_WAIT: Long = 7000
        private const val DEFERRED_WAIT: Long = 8000
    }

    //region completions
    private var onConnectionEstablishedDeferred: CompletableDeferred<Boolean>? = null
    private var onDataReadDeferred: CompletableDeferred<BleNetworkMessage>? = null
    private var onDescriptorWriteDeferred: CompletableDeferred<Boolean>? = null
    private val onDisconnectedDeferred: CompletableDeferred<Boolean>? = null
    //endregion

    //region properties
    private var readComplexResponse: Boolean = false
    private val detectedDevices: MutableList<BluetoothDevice> by lazy { mutableListOf() }

    private var searchingDevices = false
    private lateinit var bluetoothGatt: BluetoothGatt

    //endregion

    //region public methods
    internal fun getDevicesByService(serviceUUID: UUID): Flow<BluetoothDevice> {
        if (!searchingDevices) {
            searchingDevices = true
            detectedDevices.clear()
            return bleScanner.scanBleDevicesNearby(serviceUUID).onEach {
                detectedDevices += it
            }.onCompletion {
                searchingDevices = false
            }
        } else {
            Log.e("BleManager", "getDevicesByService already searching devices")
            throw GoProException(GoProError.ALREADY_SEARCHING_CAMERAS)
        }
    }

    @SuppressLint("MissingPermission")
    internal fun getPairedDevicesByPrefix(context: Context, deviceNamePrefix: String): List<BluetoothDevice> =
        context.getSystemService(BluetoothManager::class.java)
            ?.adapter
            ?.bondedDevices
            ?.filter { it.name.startsWith(deviceNamePrefix) }
            .orEmpty()

    internal fun stopSearchDevices() = bleScanner.stopSearch()

    internal suspend fun connectToDevice(context: Context, address: String): Boolean {
        return detectedDevices.firstOrNull { it.address == address }?.let {
            if (connect(context, it)) {
                subscribeToNotifications(getNotifiableCharacteristics())
                true
            } else {
                false
            }
        } ?: false
    }

    internal suspend fun sendData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        data: ByteArray,
        complexResponse: Boolean = false
    ): BleNetworkMessage = try {
        checkGatt()
        onDataReadDeferred = CompletableDeferred()
        readComplexResponse = complexResponse

        withContext(Dispatchers.IO) {
            withTimeout(DEFERRED_WAIT) {
                if (writeCharacteristic(
                        bluetoothGatt,
                        serviceUUID,
                        characteristicUUID,
                        data
                    )
                ) {
                    onDataReadDeferred?.await() ?: throw GoProException(GoProError.SEND_COMMAND_FAILED)
                } else {
                    Log.e("BleManager", "sendData writeCharacteristic failed")
                    throw GoProException(GoProError.SEND_COMMAND_FAILED)
                }
            }
        }
    } catch (e: GoProException) {
        throw e
    } catch (e: TimeoutCancellationException) {
        Log.e("BleManager", "sendData ${e.message} ${e.stackTraceToString()}")
        throw GoProException(GoProError.CAMERA_NOT_RESPONDING)
    } catch (e: Exception) {
        Log.e("BleManager", "sendData ${e.message} ${e.stackTraceToString()}")
        throw GoProException(GoProError.SEND_COMMAND_FAILED)
    }

    internal suspend fun readData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        complexResponse: Boolean = false
    ): BleNetworkMessage = try {
        checkGatt()
        onDataReadDeferred = CompletableDeferred()
        readComplexResponse = complexResponse

        withContext(Dispatchers.IO) {
            withTimeout(DEFERRED_WAIT) {
                if (readCharacteristic(bluetoothGatt, serviceUUID, characteristicUUID)) {
                    onDataReadDeferred?.await() ?: throw GoProException(GoProError.READ_DATA_FAILED)
                } else {
                    Log.e("BleManager", "readData readCharacteristic failed")
                    throw GoProException(GoProError.READ_DATA_FAILED)
                }
            }
        }
    } catch (e: GoProException) {
        throw e
    } catch (e: TimeoutCancellationException) {
        Log.e("BleManager", "readData ${e.message} ${e.stackTraceToString()}")
        throw GoProException(GoProError.CAMERA_NOT_RESPONDING)
    } catch (e: Exception) {
        Log.e("BleManager", "readData ${e.message} ${e.stackTraceToString()}")
        throw GoProException(GoProError.READ_DATA_FAILED)
    }

    //endregion

    //region private methods
    @OptIn(ExperimentalUnsignedTypes::class)
    private suspend fun connect(context: Context, device: BluetoothDevice): Boolean =
        withContext(Dispatchers.IO) {
            try {
                onConnectionEstablishedDeferred = CompletableDeferred()

                withTimeout(DEFERRED_WAIT) {
                    launchGattOperation {
                        bluetoothGatt = connectToGoProBleDevice(
                            context,
                            device,
                            onConnected = { getServices() },
                            onDisconnected = { onDisconnectedDeferred?.complete(true) },
                            onServicesDiscovered = { onConnectionEstablishedDeferred?.complete(true) },
                            onDescriptorWrite = { onDescriptorWriteDeferred?.complete(true) },
                            onCharacteristicRead = { processCharacteristic(it) },
                            onCharacteristicChanged = { _, value -> processCharacteristic(value) }
                        )
                    }
                    return@withTimeout onConnectionEstablishedDeferred?.await() ?: false
                }

            } catch (e: TimeoutCancellationException) {
                Log.e("BleManager", "connect ${e.message} ${e.stackTraceToString()}")
                throw GoProException(GoProError.CAMERA_NOT_RESPONDING)
            } catch (e: GoProException) {
                throw e
            } catch (e: Exception) {
                Log.e("BleManager", "connect ${e.message} ${e.stackTraceToString()}")
                throw GoProException(GoProError.UNABLE_TO_CONNECT_TO_CAMERA)
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
        bluetoothGatt.discoverServices()
    }

    private fun getNotifiableCharacteristics(): List<BluetoothGattCharacteristic> = bluetoothGatt.services
        ?.map { it.characteristics }
        ?.flatten()
        ?.filter { filterCharacteristicForSubscriptions(it.properties) }
        ?.filter { filterResponseCharacteristic(it) }
        .orEmpty()

    private suspend fun subscribeToNotifications(characteristics: List<BluetoothGattCharacteristic>) = try {
        characteristics.forEach { characteristic ->
            withContext(Dispatchers.IO) {
                withTimeout(DEFERRED_WAIT) {
                    onDescriptorWriteDeferred = CompletableDeferred()
                    val written = launchGattOperation {
                        writeDescriptor(characteristic, getDescriptionValueToSubscribe(characteristic))
                    }
                    if (written) {
                        onDescriptorWriteDeferred?.await()
                    } else {
                        throw GoProException(GoProError.UNABLE_TO_SUBSCRIBE_TO_NOTIFICATIONS)
                    }
                }
            }
        }
    } catch (e: GoProException) {
        throw e
    } catch (e: TimeoutCancellationException) {
        Log.e("BleManager", "subscribeToNotifications ${e.message} ${e.stackTraceToString()}")
        throw GoProException(GoProError.CAMERA_NOT_RESPONDING)
    } catch (e: Exception) {
        Log.e("BleManager", "subscribeToNotifications ${e.message} ${e.stackTraceToString()}")
        throw GoProException(GoProError.UNABLE_TO_SUBSCRIBE_TO_NOTIFICATIONS)
    }

    private fun getDescriptionValueToSubscribe(characteristic: BluetoothGattCharacteristic): ByteArray {
        return if (filterIndicatableCharacteristic(characteristic.properties)) {
            getEnableIndicationValue()
        } else {
            getEnableNotificationValue()
        }
    }

    private fun getEnableIndicationValue() = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
    private fun getEnableNotificationValue() = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE

    private fun filterIndicatableCharacteristic(properties: Int) =
        properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0

    private fun filterNotifiableCharacteristic(properties: Int) =
        properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0

    private fun filterCharacteristicForSubscriptions(properties: Int) =
        filterNotifiableCharacteristic(properties) || filterIndicatableCharacteristic(properties)


    private fun filterResponseCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        return characteristic.uuid == GoProUUID.CQ_COMMAND_RSP.uuid ||
                characteristic.uuid == GoProUUID.CQ_SETTING_RSP.uuid ||
                characteristic.uuid == GoProUUID.CQ_QUERY_RSP.uuid
    }


    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "NewApi")
    private suspend fun writeCharacteristic(
        bluetoothGatt: BluetoothGatt,
        serviceUUID: UUID,
        characteristicUUID: UUID,
        value: ByteArray
    ): Boolean {
        try {
            val characteristic = bluetoothGatt.getService(serviceUUID)?.getCharacteristic(characteristicUUID)
            if (characteristic != null) {
                return if (buildVersionProvider.getSdkVersion() >= Build.VERSION_CODES.TIRAMISU) {

                    launchGattOperation {
                        bluetoothGatt.writeCharacteristic(
                            characteristic,
                            value,
                            BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
                        ) == BluetoothStatusCodes.SUCCESS
                    }
                } else {
                    launchGattOperation {
                        bluetoothGatt.writeCharacteristic(characteristic.apply { this.value = value })
                    }
                }
            } else {
                Log.e("BleManager", "writeCharacteristic characteristic is null")
                throw GoProException(GoProError.SEND_COMMAND_FAILED)
            }
        } catch (e: Exception) {
            Log.e("BleManager", "writeCharacteristic ${e.message} ${e.stackTraceToString()}")
            throw GoProException(GoProError.OTHER)
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun readCharacteristic(
        bluetoothGatt: BluetoothGatt,
        serviceUUID: UUID,
        characteristicUUID: UUID
    ): Boolean {
        try {
            val characteristic = launchGattOperation {
                bluetoothGatt.getService(serviceUUID)?.getCharacteristic(characteristicUUID)
            }
            return launchGattOperation {
                bluetoothGatt.readCharacteristic(characteristic)
            }
        } catch (e: Exception) {
            Log.e("BleManager", "readCharacteristic ${e.message} ${e.stackTraceToString()}")
            throw GoProException(GoProError.OTHER)
        }
    }

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "NewApi")
    private fun writeDescriptor(characteristic: BluetoothGattCharacteristic, descriptorValue: ByteArray): Boolean = try {
        bluetoothGatt.setCharacteristicNotification(characteristic, true)
        characteristic.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID))?.let { descriptor ->
            return if (buildVersionProvider.getSdkVersion() >= Build.VERSION_CODES.TIRAMISU) {
                bluetoothGatt.writeDescriptor(descriptor, descriptorValue) == BluetoothStatusCodes.SUCCESS
            } else {
                bluetoothGatt.writeDescriptor(descriptor.apply { value = descriptorValue })
            }
        } ?: false
    } catch (e: GoProException) {
        throw e
    } catch (ex: Exception) {
        Log.e("BleManager", "writeDescriptor ${ex.message} ${ex.stackTraceToString()}")
        throw GoProException(GoProError.UNABLE_TO_SUBSCRIBE_TO_NOTIFICATIONS)
    }


    private fun checkGatt() {
        if (::bluetoothGatt.isInitialized.not()) {
            throw GoProException(GoProError.CAMERA_NOT_CONNECTED)
        }
    }

    private suspend fun <T> launchGattOperation(operation: suspend () -> T): T {
        return gattMutex.withLock {
            try {
                withTimeout(MUTEX_WAIT) {
                    runBlocking { operation() }
                }
            } catch (e: GoProException) {
                throw e
            } catch (e: Exception) {
                Log.e("BleManager", "launchGattOperation ERROR ${e.message} ${e.stackTraceToString()}")
                throw GoProException(GoProError.COMMUNICATION_FAILED)
            }
        }
    }

//endregion
}