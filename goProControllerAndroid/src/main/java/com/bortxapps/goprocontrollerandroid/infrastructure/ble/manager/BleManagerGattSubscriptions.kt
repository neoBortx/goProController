package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothStatusCodes
import android.os.Build
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.BLE_DESCRIPTION_BASE_UUID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.BleManagerGattOperationBase
import com.bortxapps.goprocontrollerandroid.utils.BuildVersionProvider
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.withContext
import java.util.UUID

internal class BleManagerGattSubscriptions(
    private val bleManagerGattCallBacks: BleManagerGattCallBacks,
    private val buildVersionProvider: BuildVersionProvider,
    gattMutex: Mutex,
    bleConfiguration: BleConfiguration
) : BleManagerGattOperationBase(gattMutex, bleConfiguration) {

    internal suspend fun subscribeToNotifications(bluetoothGatt: BluetoothGatt) {
        getNotifiableCharacteristics(bluetoothGatt).forEach { characteristic ->
            withContext(IO) {
                launchGattOperation {
                    bleManagerGattCallBacks.initWriteDescriptorOperation()
                    if (writeDescriptor(characteristic, getDescriptionValueToSubscribe(characteristic), bluetoothGatt)) {
                        launchDeferredOperation {
                            bleManagerGattCallBacks.waitForWrittenDescriptor()
                        }
                    } else {
                        throw GoProException(GoProError.UNABLE_TO_SUBSCRIBE_TO_NOTIFICATIONS)
                    }

                }
            }
        }
    }


    private fun getNotifiableCharacteristics(bluetoothGatt: BluetoothGatt): List<BluetoothGattCharacteristic> = bluetoothGatt.services
        ?.map { it.characteristics }
        ?.flatten()
        ?.filter { filterCharacteristicForSubscriptions(it.properties) }
        ?.filter { filterResponseCharacteristic(it) }
        .orEmpty()

    @Suppress("DEPRECATION")
    @SuppressLint("MissingPermission", "NewApi")
    private fun writeDescriptor(characteristic: BluetoothGattCharacteristic, descriptorValue: ByteArray, bluetoothGatt: BluetoothGatt): Boolean =
        try {
            bluetoothGatt.setCharacteristicNotification(characteristic, true)
            characteristic.getDescriptor(UUID.fromString(BLE_DESCRIPTION_BASE_UUID))?.let { descriptor ->
                return if (buildVersionProvider.getSdkVersion() >= Build.VERSION_CODES.TIRAMISU) {
                    bluetoothGatt.writeDescriptor(descriptor, descriptorValue) == BluetoothStatusCodes.SUCCESS
                } else {
                    bluetoothGatt.writeDescriptor(descriptor.apply { value = descriptorValue })
                }
            } ?: false
        } catch (ex: Exception) {
            Log.e("BleManager", "writeDescriptor ${ex.message} ${ex.stackTraceToString()}")
            throw GoProException(GoProError.UNABLE_TO_SUBSCRIBE_TO_NOTIFICATIONS)
        }

    private fun filterCharacteristicForSubscriptions(properties: Int) =
        filterNotifiableCharacteristic(properties) || filterIndicatableCharacteristic(properties)

    private fun filterNotifiableCharacteristic(properties: Int) =
        properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0

    private fun filterIndicatableCharacteristic(properties: Int) =
        properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0

    private fun filterResponseCharacteristic(characteristic: BluetoothGattCharacteristic): Boolean {
        return characteristic.uuid == GoProUUID.CQ_COMMAND_RSP.uuid ||
                characteristic.uuid == GoProUUID.CQ_SETTING_RSP.uuid ||
                characteristic.uuid == GoProUUID.CQ_QUERY_RSP.uuid
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
}