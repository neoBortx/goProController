package com.bortxapps.goprocontrollerandroid.feature.connection.mapper

import com.bortxapps.goprocontrollerandroid.domain.data.GoProBleConnectionStatus
import com.bortxapps.simplebleclient.api.data.BleConnectionStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class GoProBleConnectionStateMapperTest(
    private val inputState: BleConnectionStatus,
    private val expectedOutput: GoProBleConnectionStatus
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with inputState={0}, expectedOutput={1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(BleConnectionStatus.DISCONNECTED, GoProBleConnectionStatus.STATE_DISCONNECTED),
                arrayOf(BleConnectionStatus.CONNECTING, GoProBleConnectionStatus.STATE_CONNECTING),
                arrayOf(BleConnectionStatus.CONNECTED, GoProBleConnectionStatus.STATE_CONNECTED),
                arrayOf(BleConnectionStatus.DISCONNECTING, GoProBleConnectionStatus.STATE_DISCONNECTING),
                arrayOf(BleConnectionStatus.UNKNOWN, GoProBleConnectionStatus.UNKNOWN)
            )
        }
    }

    @Test
    fun testGoProBleConnectionStateMapper() {
        val result = goProBleConnectionStateMapper(inputState)
        assertEquals(expectedOutput, result)
    }
}
