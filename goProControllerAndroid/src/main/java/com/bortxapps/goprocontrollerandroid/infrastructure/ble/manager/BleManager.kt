package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
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
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.BleDeviceScanner
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.connectToGoProBleDevice
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.getBluetoothAdapter
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

class BleManager private constructor(
    private val bleNetworkMessageProcessor: BleNetworkMessageProcessor = BleNetworkMessageProcessor(),
    private val bleScanner: BleDeviceScanner = BleDeviceScanner(),
    private val gattMutex: Mutex = Mutex()
) {

    companion object {
        val instance: BleManager by lazy {
            BleManager()
        }

        private const val MUTEX_WAIT: Long = 10000
    }

    //region completions
    private var onConnectionEstablishedDeferred: CompletableDeferred<Boolean>? = null
    private var onDataReadDeferred: CompletableDeferred<BleNetworkMessage>? = null
    private var onDescriptorWriteDeferred: CompletableDeferred<Boolean>? = null
    private val onDisconnectedDeferred: CompletableDeferred<Boolean>? = null
    private var readComplexResponse: Boolean = false
    private val detectedDevices: MutableList<BluetoothDevice> = mutableListOf()
    private lateinit var bluetoothGatt: BluetoothGatt
    //endregion

    //region public methods
    internal fun getDevicesByService(context: Context, serviceUUID: UUID): Flow<BluetoothDevice> {
        detectedDevices.clear()
        return bleScanner.scanBleDevicesNearby(context, serviceUUID).onEach {
            detectedDevices += it
        }
    }

    @SuppressLint("MissingPermission")
    internal fun getPairedDevicesByPrefix(context: Context, deviceNamePrefix: String): List<BluetoothDevice> =
        getBluetoothAdapter(context)?.bondedDevices
            ?.filter { it.name.startsWith(deviceNamePrefix) }
            .orEmpty()

    internal fun stopSearchDevices(context: Context) = bleScanner.stopSearch(context)

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
    ): BleNetworkMessage {
        checkGatt()
        onDataReadDeferred = CompletableDeferred()
        readComplexResponse = complexResponse
        return if (writeCharacteristic(
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

    internal suspend fun readData(
        serviceUUID: UUID,
        characteristicUUID: UUID,
        complexResponse: Boolean = false
    ): BleNetworkMessage {
        checkGatt()
        return withContext(Dispatchers.IO) {
            onDataReadDeferred = CompletableDeferred()
            readComplexResponse = complexResponse
            readCharacteristic(bluetoothGatt, serviceUUID, characteristicUUID)
            onDataReadDeferred?.await() ?: throw GoProException(GoProError.SEND_COMMAND_FAILED)
        }
    }
    //endregion

    //region private methods
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
                        onCharacteristicChanged = { _, value -> processCharacteristic(value) }
                    )
                }
                return@withContext onConnectionEstablishedDeferred?.await() ?: false
            } catch (e: Exception) {
                Log.e("BleManager", "writeCharacteristic ${e.message} ${e.stackTraceToString()}")
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
        bluetoothGatt.discoverServices()
    }

    private fun getNotifiableCharacteristics(): List<BluetoothGattCharacteristic> = bluetoothGatt.services
        ?.map { it.characteristics }
        ?.flatten()
        ?.filter { filterCharacteristicForSubscriptions(it.properties) }
        ?.filter { filterResponseCharacteristic(it) }
        .orEmpty()

    private suspend fun subscribeToNotifications(characteristics: List<BluetoothGattCharacteristic>) {
        characteristics.forEach { characteristic ->
            launchGattOperation {
                writeDescriptor(characteristic, getDescriptionValueToSubscribe(characteristic))
            }
        }
    }

    private fun getDescriptionValueToSubscribe(characteristic: BluetoothGattCharacteristic): ByteArray {
        return if (filterIndicatableCharacteristic(characteristic.properties)) {
            BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
        } else {
            BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        }
    }

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


    @SuppressLint("MissingPermission")
    private suspend fun writeCharacteristic(
        bluetoothGatt: BluetoothGatt,
        serviceUUID: UUID,
        characteristicUUID: UUID,
        value: ByteArray
    ): Boolean {
        try {
            val characteristic = bluetoothGatt.getService(serviceUUID)?.getCharacteristic(characteristicUUID)
            if (characteristic != null) {
                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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

    @SuppressLint("MissingPermission")
    private suspend fun writeDescriptor(characteristic: BluetoothGattCharacteristic, descriptorValue: ByteArray) {
        onDescriptorWriteDeferred = CompletableDeferred()
        try {
            bluetoothGatt.setCharacteristicNotification(characteristic, true)
            characteristic.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID))?.let { descriptor ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    val res = bluetoothGatt.writeDescriptor(descriptor, descriptorValue)
                    if (res != BluetoothStatusCodes.SUCCESS) {
                        Log.e("BleManager", "subscribeToNotifications writeDescriptor failed -> $res")
                    } else {
                        Log.d("BleManager", "subscribeToNotifications writeDescriptor success")
                    }
                } else {
                    bluetoothGatt.writeDescriptor(descriptor.apply { value = descriptorValue })
                }
            }
        } catch (ex: Exception) {
            Log.e("BleManager", "writeDescriptor ${ex.message} ${ex.stackTraceToString()}")
            throw GoProException(GoProError.OTHER)
        } finally {
            onDescriptorWriteDeferred?.await()
        }
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
            } catch (e: Exception) {
                Log.e("BleManager", "launchGattOperation ${e.message} ${e.stackTraceToString()}")
                throw GoProException(GoProError.OTHER)
            }
        }
    }

    //endregion
}