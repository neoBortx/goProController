package com.bortxapps.goprocontrollerandroid.feature.connection

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothProfile
import android.content.Context
import app.cash.turbine.test
import com.bortxapps.goprocontrollerandroid.domain.data.GoProBleConnectionStatus
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GOPRO_NAME_PREFIX
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.feature.connection.api.ConnectionApi
import com.bortxapps.goprocontrollerandroid.feature.connection.mapper.toMapCamera
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.checkBleHardwareAvailable
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.checkBluetoothEnabled
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.checkPermissions
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.checkPermissionsApiCodeS
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils.checkPermissionsOldApi
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.runs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GoProConnectorImplTest {

    private lateinit var api: ConnectionApi
    private lateinit var connector: GoProConnectorImpl
    private val context = mockk<Context>(relaxed = true)

    @Before
    fun setUp() {
        mockkStatic(::checkBluetoothEnabled)
        mockkStatic(::checkPermissionsApiCodeS)
        mockkStatic(::checkPermissionsOldApi)
        mockkStatic(::checkPermissions)
        mockkStatic(::checkBleHardwareAvailable)

        coEvery { checkBluetoothEnabled(any()) } returns Unit
        coEvery { checkPermissions(any()) } returns Unit
        coEvery { checkBleHardwareAvailable(any()) } returns Unit

        api = mockk(relaxed = true)
        connector = GoProConnectorImpl(context, api)


    }

    @Test
    fun `stopSearch returns success when API call is successful`() = runTest {
        coEvery { api.stopSearch() } just runs // Mocking the stopSearch method

        val result = connector.stopSearch()

        assertEquals(Result.success(true), result)
        coVerify(exactly = 1) { api.stopSearch() }
    }

    @Test
    fun `getCamerasPaired returns list of paired cameras`() = runTest {
        val mockBluetoothDevice = mockk<BluetoothDevice>(relaxed = true)
        val expectedCameraList = listOf(mockBluetoothDevice.toMapCamera())

        coEvery { api.getPairedCameras(context, any()) } returns listOf(mockBluetoothDevice)

        val result = connector.getCamerasPaired().getOrThrow().toList()

        assertEquals(expectedCameraList, result)
        coVerify(exactly = 1) { api.getPairedCameras(context, GOPRO_NAME_PREFIX) }
    }

    @Test
    fun `getNearByCameras returns list of nearby cameras`() = runTest {
        val mockBluetoothDevice = mockk<BluetoothDevice>(relaxed = true)
        val expectedCameraList = listOf(mockBluetoothDevice.toMapCamera())

        coEvery { api.getNearByCameras(GoProUUID.SERVICE_UUID.uuid) } returns flowOf(mockBluetoothDevice)

        val result = connector.getNearByCameras().getOrThrow().toList()

        assertEquals(expectedCameraList, result)
        coVerify(exactly = 1) { api.getNearByCameras(GoProUUID.SERVICE_UUID.uuid) }
    }

    @Test
    fun `disconnectBle calls api disconnectBle and returns success`() = runTest {
        coEvery { api.disconnectBle() } just runs

        val result = connector.disconnectBle()

        assertEquals(Result.success(true), result)
        coVerify(exactly = 1) { api.disconnectBle() }
    }

    @Test
    fun `subscribeToBleConnectionStatusChanges transforms and emits correct values`() = runTest {

        val mockStatusFlow = MutableStateFlow(BluetoothProfile.STATE_DISCONNECTED)

        every { api.subscribeToConnectionStatusChanges() } returns mockStatusFlow

        connector.subscribeToBleConnectionStatusChanges().test {
            assertEquals(GoProBleConnectionStatus.STATE_DISCONNECTED, awaitItem())

            mockStatusFlow.value = BluetoothProfile.STATE_CONNECTING
            assertEquals(GoProBleConnectionStatus.STATE_CONNECTING, awaitItem())

            mockStatusFlow.value = BluetoothProfile.STATE_CONNECTED
            assertEquals(GoProBleConnectionStatus.STATE_CONNECTED, awaitItem())

            mockStatusFlow.value = BluetoothProfile.STATE_DISCONNECTING
            assertEquals(GoProBleConnectionStatus.STATE_DISCONNECTING, awaitItem())
        }
    }

    @Test
    fun `connectToDevice should return failure for general Exception`() = runTest {
        val address = "TestAddress"
        coEvery { api.connectToDevice(context, address) } throws Exception("Test Exception")

        val result = connector.connectToDevice(address)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is GoProException)
    }

    @Test
    fun `connectToDevice should return failure for GoProException`() = runTest {
        val address = "TestAddress"
        val goProException = GoProException(GoProError.OTHER)
        coEvery { api.connectToDevice(context, address) } throws goProException

        val result = connector.connectToDevice(address)
        assertTrue(result.isFailure)
        assertEquals(goProException, result.exceptionOrNull())
    }

    @Test
    fun `connectToDevice should return success when api returns true`() = runTest {
        val address = "TestAddress"
        coEvery { api.connectToDevice(context, address) } returns true

        val result = connector.connectToDevice(address)
        assertTrue(result.isSuccess)
        assertEquals(true, result.getOrNull())
    }

    @Test
    fun `connectToDevice should return failure when api returns false`() = runTest {
        val address = "TestAddress"
        coEvery { api.connectToDevice(context, address) } returns false

        val result = connector.connectToDevice(address)
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is GoProException)
    }




}
