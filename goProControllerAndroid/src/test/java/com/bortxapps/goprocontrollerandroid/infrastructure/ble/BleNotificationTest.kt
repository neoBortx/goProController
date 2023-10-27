package com.bortxapps.goprocontrollerandroid.infrastructure.ble

import app.cash.turbine.test
import com.juul.kable.DiscoveredCharacteristic
import com.juul.kable.Peripheral
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import java.util.UUID

class BleNotificationTest {

    @org.junit.Test
    fun testEnableNotifications_servicesReturnsNull_expectEmptyFlow() = runBlocking {
        val peripheral: Peripheral = mockk()

        coEvery {
            peripheral.services
        } returns null

        enableNotifications(peripheral).test {
            awaitComplete()
        }
    }

    @org.junit.Test
    fun testEnableNotifications_servicesReturnsEmpty_expectEmptyFlow() = runBlocking {
        val peripheral: Peripheral = mockk()

        coEvery {
            peripheral.services
        } returns listOf()

        enableNotifications(peripheral).test {
            awaitComplete()
        }
    }

    @org.junit.Test
    fun testEnableNotifications_servicesReturnsSomeServices_expectFlow() = runBlocking {
        val peripheral: Peripheral = mockk()
        val characteristic: DiscoveredCharacteristic = mockk()

        val serviceId = UUID.randomUUID()
        val characteristicId = UUID.randomUUID()
        val resultBytes = byteArrayOf(0x03, 0x02)
        val characteristics = listOf(characteristic)


        mockkStatic(::getNotifiableServices)

        every { characteristic.serviceUuid } answers { serviceId }
        every { characteristic.characteristicUuid } answers { characteristicId }

        every { peripheral.observe(characteristic, any()) } returns flow {
            emit(resultBytes)
        }
        every { getNotifiableServices(peripheral) } returns characteristics



        enableNotifications(peripheral).test {
            val response = awaitItem()
            assertEquals(resultBytes, response.first)
            assertEquals(serviceId, response.second)
            assertEquals(characteristicId, response.third)
            awaitComplete()
        }
    }
}