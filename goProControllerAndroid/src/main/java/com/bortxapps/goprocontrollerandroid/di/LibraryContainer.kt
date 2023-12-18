package com.bortxapps.goprocontrollerandroid.di

import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProCommands
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProConnector
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProMedia
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.GoProCommandsImpl
import com.bortxapps.goprocontrollerandroid.feature.commands.api.CommandsApi
import com.bortxapps.goprocontrollerandroid.feature.connection.GoProConnectorImpl
import com.bortxapps.goprocontrollerandroid.feature.connection.api.ConnectionApi
import com.bortxapps.goprocontrollerandroid.feature.media.GoProMediaImpl
import com.bortxapps.goprocontrollerandroid.feature.media.api.MediaApi
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessageProcessor
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager
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
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiManager
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.KtorClient
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.getKtorHttpClient
import com.bortxapps.goprocontrollerandroid.urils.BuildVersionProvider
import kotlinx.coroutines.sync.Mutex

internal class LibraryContainer(context: Context) {

    //wifi
    private val wifiManager = WifiManager()
    private val httpClient = getKtorHttpClient()
    private val ktorHttpClient = KtorClient(httpClient)

    //ble
    private val blueToothScanner = getSystemService(context.applicationContext, BluetoothManager::class.java)?.adapter?.bluetoothLeScanner
        ?: throw GoProException(GoProError.UNABLE_INITIALIZE_CONTROLLER)

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

    private val bleManagerGattCallBacks = BleManagerGattCallBacks(bleNetworkMessageProcessor)
    private val bleManagerDeviceSearchOperations = BleManagerDeviceSearchOperations(bleDeviceScannerManager)
    private val bleManagerGattConnectionOperations =
        BleManagerGattConnectionOperations(bleManagerDeviceSearchOperations, bleManagerGattCallBacks, gattMutex)
    private val bleManagerGattSubscriptions = BleManagerGattSubscriptions(bleManagerGattCallBacks, buildVersionProvider, gattMutex)
    private val bleManagerGattWriteOperations = BleManagerGattWriteOperations(bleManagerGattCallBacks, buildVersionProvider, gattMutex)
    private val bleManagerGattReadOperations = BleManagerGattReadOperations(bleManagerGattCallBacks, gattMutex)


    private val bleManager = BleManager(
        bleManagerDeviceSearchOperations,
        bleManagerGattConnectionOperations,
        bleManagerGattSubscriptions,
        bleManagerGattReadOperations,
        bleManagerGattWriteOperations,
        bleManagerGattCallBacks
    )

    //features api
    private val mediaApi = MediaApi(ktorHttpClient)
    private val connectionApi = ConnectionApi(bleManager)
    private val commandsApi = CommandsApi(bleManager)

    //features repository
    val goProMediaImpl: GoProMedia = GoProMediaImpl(context.applicationContext, mediaApi, wifiManager)
    val goProConnectorImpl: GoProConnector = GoProConnectorImpl(context.applicationContext, connectionApi)
    val goProCommandsImpl: GoProCommands = GoProCommandsImpl(context.applicationContext, commandsApi)
}