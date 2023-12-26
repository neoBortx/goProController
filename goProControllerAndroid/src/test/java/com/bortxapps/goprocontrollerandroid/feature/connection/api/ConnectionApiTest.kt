package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.simplebleclient.manager.contracts.SimpleBleClient
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.util.UUID

class ConnectionApiTest {

    private lateinit var bleManager: SimpleBleClient
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
    fun getNearByCameras_callsBleManager() = runTest {
        connectionApi.getNearByCameras(serviceUUID)

        coVerify { bleManager.getDevicesByService(serviceUUID) }
    }

    @Test
    fun stopSearch_callsBleManager() = runTest  {
        connectionApi.stopSearch()

        coVerify { bleManager.stopSearchDevices() }
    }

    @Test
    fun getPairedCameras_callsBleManager() = runTest  {
        connectionApi.getPairedCameras(context, deviceNamePrefix)

        coVerify { bleManager.getPairedDevicesByPrefix(context, deviceNamePrefix) }
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
