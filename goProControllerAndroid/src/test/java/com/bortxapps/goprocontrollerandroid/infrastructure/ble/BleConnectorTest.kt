package com.bortxapps.goprocontrollerandroid.infrastructure.ble

import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleError
import com.juul.kable.ConnectionRejectedException
import com.juul.kable.Peripheral
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.lang.Exception
import java.util.UUID

class BleConnectorTest {

    //region connectToCamera
    @Test
    fun testConnectToCamera_peripheralTrowsConnectionRejectedException_expectError() {
        val peripheral: Peripheral = mockk()
        val scope: CoroutineScope = mockk()
        val identifier = UUID.randomUUID().toString()

        coEvery {
            peripheral.connect()
        } throws ConnectionRejectedException()

        val result = runBlocking { connectToCamera(identifier, scope, peripheral) }

        TestCase.assertTrue(result.isLeft())
        result.onLeft {
            TestCase.assertEquals(BleError.DEVICE_NOT_CONNECTED, it)
        }
    }

    @Test
    fun testConnectToCamera_peripheralTrowsGenericException_expectError() {
        val peripheral: Peripheral = mockk()
        val scope: CoroutineScope = mockk()
        val identifier = UUID.randomUUID().toString()

        coEvery {
            peripheral.connect()
        } throws Exception()

        val result = runBlocking { connectToCamera(identifier, scope, peripheral) }

        TestCase.assertTrue(result.isLeft())
        result.onLeft {
            TestCase.assertEquals(BleError.OTHER, it)
        }
    }

    @Test
    fun testConnectToCamera_peripheralReturnsUnit_expectSuccess() {
        val peripheral: Peripheral = mockk()
        val scope: CoroutineScope = mockk()
        val identifier = UUID.randomUUID().toString()
        val returnValue = Unit

        coEvery {
            peripheral.connect()
        } returns returnValue

        val result = runBlocking { connectToCamera(identifier, scope, peripheral) }

        TestCase.assertTrue(result.isRight())
        result.onRight {
            TestCase.assertEquals(returnValue, it)
        }
    }
    //endregion

    //region disconnectCamera
    @Test
    fun testDisconnectCamera_peripheralTrowsGenericException_expectError() {
        val peripheral: Peripheral = mockk()

        coEvery {
            peripheral.disconnect()
        } throws Exception()

        val result = runBlocking { disconnectCamera(peripheral) }

        TestCase.assertTrue(result.isLeft())
        result.onLeft {
            TestCase.assertEquals(BleError.OTHER, it)
        }
    }

    @Test
    fun testDisconnectCamera_peripheralReturnsUnit_expectSuccess() {
        val peripheral: Peripheral = mockk()
        val returnValue = Unit

        coEvery {
            peripheral.disconnect()
        } returns returnValue

        val result = runBlocking { disconnectCamera(peripheral) }

        TestCase.assertTrue(result.isRight())
        result.onRight {
            TestCase.assertEquals(returnValue, it)
        }
    }
    //endregion
}