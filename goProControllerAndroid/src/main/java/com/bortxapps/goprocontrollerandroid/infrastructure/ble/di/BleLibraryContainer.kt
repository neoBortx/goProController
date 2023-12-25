package com.bortxapps.goprocontrollerandroid.infrastructure.ble.di

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessageProcessor
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.BleError
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.SimpleBleClientException
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleConfiguration
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManagerDeviceSearchOperations
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManagerGattCallBacks
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManagerGattConnectionOperations
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManagerGattReadOperations
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManagerGattSubscriptions
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManagerGattWriteOperations
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner.BleDeviceScannerCallbackBuilder
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner.BleDeviceScannerFilterBuilder
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner.BleDeviceScannerManager
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.scanner.BleDeviceScannerSettingsBuilder
import com.bortxapps.goprocontrollerandroid.utils.BuildVersionProvider
import kotlinx.coroutines.sync.Mutex

internal class BleLibraryContainer(context: Context) {
    //ble
    private val blueToothScanner = ContextCompat.getSystemService(context.applicationContext, BluetoothManager::class.java)?.adapter?.bluetoothLeScanner
        ?: throw SimpleBleClientException(BleError.UNABLE_INITIALIZE_CONTROLLER)

    private val bleDeviceScannerFilterBuilder = BleDeviceScannerFilterBuilder()
    private val bleDeviceScannerSettingsBuilder = BleDeviceScannerSettingsBuilder()
    private val bleDeviceScannerCallbackBuilder = BleDeviceScannerCallbackBuilder()
    private val bleDeviceScannerManager = BleDeviceScannerManager(
        blueToothScanner,
        bleDeviceScannerSettingsBuilder,
        bleDeviceScannerFilterBuilder,
        bleDeviceScannerCallbackBuilder
    )

    private val bleNetworkMessageProcessor = BleNetworkMessageProcessor()
    private val gattMutex = Mutex()
    private val buildVersionProvider = BuildVersionProvider()

    val bleConfiguration = BleConfiguration()
    val bleManagerGattCallBacks = BleManagerGattCallBacks(bleNetworkMessageProcessor)
    val bleManagerDeviceSearchOperations = BleManagerDeviceSearchOperations(bleDeviceScannerManager)
    val bleManagerGattConnectionOperations =
        BleManagerGattConnectionOperations(bleManagerDeviceSearchOperations, bleManagerGattCallBacks, gattMutex, bleConfiguration)
    val bleManagerGattSubscriptions = BleManagerGattSubscriptions(bleManagerGattCallBacks, buildVersionProvider, gattMutex, bleConfiguration)
    val bleManagerGattWriteOperations =
        BleManagerGattWriteOperations(bleManagerGattCallBacks, buildVersionProvider, gattMutex, bleConfiguration)
    val bleManagerGattReadOperations = BleManagerGattReadOperations(bleManagerGattCallBacks, gattMutex, bleConfiguration)
}