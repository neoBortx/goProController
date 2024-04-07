package com.bortxapps.goprocontrollerandroid.feature.connection.api

import android.content.Context
import com.bortxapps.simplebleclient.api.contracts.SimpleBleClient
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

        coVerify { bleManager.deviceSeeker.getDevicesNearby(serviceUUID) }
    }

    @Test
    fun stopSearch_callsBleManager() = runTest  {
        connectionApi.stopSearch()

        coVerify { bleManager.deviceSeeker.stopSearchDevices() }
    }

    @Test
    fun getPairedCameras_callsBleManager() = runTest  {
        connectionApi.getPairedCameras(context, deviceNamePrefix)

        coVerify { bleManager.deviceSeeker.getPairedDevices(context) }
    }

    @Test
    fun connectToDevice_callsBleManager() = runBlocking {
        connectionApi.connectToDevice(context, deviceAddress)

        coVerify { bleManager.connection.connectToDevice(context, deviceAddress) }
    }

    @Test
    fun subscribeToConnectionStatusChanges_callsBleManager() {
        connectionApi.subscribeToConnectionStatusChanges()

        verify { bleManager.connection.subscribeToConnectionStatusChanges() }
    }

    @Test
    fun disconnectBle_callsBleManager() = runBlocking {
        connectionApi.disconnectBle()

        coVerify { bleManager.connection.disconnect() }
    }
}
