package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ConnectionApiTest {

    private lateinit var bleManager: BleManager
    private lateinit var connectionApi: ConnectionApi
    private lateinit var context: Context
    private val serviceUUID = UUID.randomUUID()
    private val deviceAddress = "DeviceAddress"
    private val deviceNamePrefix = "GoPro"

    @Before
    fun setUp() {
        bleManager = mockk(relaxed = true)
        context = mockk(relaxed = true)
        connectionApi = ConnectionApi(bleManager)
    }

    @Test
    fun getNearByCameras_callsBleManager() {
        connectionApi.getNearByCameras(serviceUUID)

        verify { bleManager.getDevicesByService(serviceUUID) }
    }

    @Test
    fun stopSearch_callsBleManager() {
        connectionApi.stopSearch()

        verify { bleManager.stopSearchDevices() }
    }

    @Test
    fun getPairedCameras_callsBleManager() {
        connectionApi.getPairedCameras(context, deviceNamePrefix)

        verify { bleManager.getPairedDevicesByPrefix(context, deviceNamePrefix) }
    }

    @Test
    fun connectToDevice_callsBleManager() = runBlocking {
        connectionApi.connectToDevice(context, deviceAddress)

        coVerify { bleManager.connectToDevice(context, deviceAddress) }
    }

    @Test
    fun subscribeToConnectionStatusChanges_callsBleManager() {
        connectionApi.subscribeToConnectionStatusChanges()

        verify { bleManager.subscribeToConnectionStatusChanges() }
    }

    @Test
    fun disconnectBle_callsBleManager() = runBlocking {
        connectionApi.disconnectBle()

        coVerify { bleManager.disconnect() }
    }
}
