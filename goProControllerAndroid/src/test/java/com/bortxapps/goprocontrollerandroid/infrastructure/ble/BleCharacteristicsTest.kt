package com.bortxapps.goprocontrollerandroid.infrastructure.ble

import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleError
import com.juul.kable.NotReadyException
import com.juul.kable.Peripheral
import com.juul.kable.characteristicOf
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.util.UUID

class BleCharacteristicsTest {

    //region readCharacteristic
    @Test
    fun testReadCharacteristic_peripheralTrowsNotReadyExceptionException_expectError() {
        val peripheral: Peripheral = mockk()
        val gattServiceUuid: UUID = UUID.randomUUID()
        val gattCharacteristicUuid: UUID = UUID.randomUUID()

        coEvery {
            peripheral.read(
                characteristicOf(
                    gattServiceUuid.toString(),
                    gattCharacteristicUuid.toString()
                )
            )
        } throws NotReadyException()

        val result =
            runBlocking { readCharacteristic(peripheral, gattServiceUuid, gattCharacteristicUuid) }

        assertTrue(result.isLeft())
        result.onLeft {
            assertEquals(BleError.DEVICE_NOT_CONNECTED, it)
        }
    }

    @Test
    fun testReadCharacteristic_peripheralTrowsGenericException_expectError() {
        val peripheral: Peripheral = mockk()
        val gattServiceUuid: UUID = UUID.randomUUID()
        val gattCharacteristicUuid: UUID = UUID.randomUUID()

        coEvery {
            peripheral.read(
                characteristicOf(
                    gattServiceUuid.toString(),
                    gattCharacteristicUuid.toString()
                )
            )
        } throws Exception()

        val result =
            runBlocking { readCharacteristic(peripheral, gattServiceUuid, gattCharacteristicUuid) }

        assertTrue(result.isLeft())
        result.onLeft {
            assertEquals(BleError.OTHER, it)
        }
    }

    @Test
    fun testReadCharacteristic_peripheralReturnsValue_expectValue() {
        val peripheral: Peripheral = mockk()
        val gattServiceUuid: UUID = UUID.randomUUID()
        val gattCharacteristicUuid: UUID = UUID.randomUUID()
        val response: ByteArray = byteArrayOf(0x00, 0x01)

        coEvery {
            peripheral.read(
                characteristicOf(
                    gattServiceUuid.toString(),
                    gattCharacteristicUuid.toString()
                )
            )
        } returns response

        val result =
            runBlocking { readCharacteristic(peripheral, gattServiceUuid, gattCharacteristicUuid) }

        assertTrue(result.isRight())
        result.onRight {
            assertEquals(response.toString(), it)
        }
    }
    //endregion

    //region writeCharacteristic
    @Test
    fun testWriteCharacteristic_peripheralTrowsGenericException_expectError() {
        val peripheral: Peripheral = mockk()
        val gattServiceUuid: UUID = UUID.randomUUID()
        val gattCharacteristicUuid: UUID = UUID.randomUUID()
        val value: ByteArray = byteArrayOf(0x00, 0x01)

        coEvery {
            peripheral.write(
                characteristicOf(
                    gattServiceUuid.toString(),
                    gattCharacteristicUuid.toString()
                ),
                value
            )
        } throws Exception()

        val result = runBlocking {
            writeCharacteristic(
                peripheral,
                gattServiceUuid,
                gattCharacteristicUuid,
                value
            )
        }

        assertTrue(result.isLeft())
        result.onLeft {
            assertEquals(BleError.OTHER, it)
        }
    }

    @Test
    fun testWriteCharacteristic_peripheralTrowsNotReadyException_expectError() {
        val peripheral: Peripheral = mockk()
        val gattServiceUuid: UUID = UUID.randomUUID()
        val gattCharacteristicUuid: UUID = UUID.randomUUID()
        val value: ByteArray = byteArrayOf(0x00, 0x01)

        coEvery {
            peripheral.write(
                characteristicOf(
                    gattServiceUuid.toString(),
                    gattCharacteristicUuid.toString()
                ),
                value
            )
        } throws NotReadyException()

        val result = runBlocking {
            writeCharacteristic(
                peripheral,
                gattServiceUuid,
                gattCharacteristicUuid,
                value
            )
        }

        assertTrue(result.isLeft())
        result.onLeft {
            assertEquals(BleError.DEVICE_NOT_CONNECTED, it)
        }
    }

    @Test
    fun testWriteCharacteristic_peripheralReturnsUnit_expectSuccess() {
        val peripheral: Peripheral = mockk()
        val gattServiceUuid: UUID = UUID.randomUUID()
        val gattCharacteristicUuid: UUID = UUID.randomUUID()
        val value: ByteArray = byteArrayOf(0x00, 0x01)
        val response = Unit

        coEvery {
            peripheral.write(
                characteristicOf(
                    gattServiceUuid.toString(),
                    gattCharacteristicUuid.toString()
                ),
                value
            )
        } returns response

        val result = runBlocking {
            writeCharacteristic(
                peripheral,
                gattServiceUuid,
                gattCharacteristicUuid,
                value
            )
        }

        assertTrue(result.isRight())
        result.onRight {
            assertEquals(response, it)
        }
    }
    //endregion
}