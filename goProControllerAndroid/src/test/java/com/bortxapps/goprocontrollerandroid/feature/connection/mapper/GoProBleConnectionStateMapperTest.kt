package com.bortxapps.goprocontrollerandroid.feature.connection.mapper

import android.bluetooth.BluetoothProfile
import com.bortxapps.goprocontrollerandroid.domain.data.GoProBleConnectionStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class GoProBleConnectionStateMapperTest(
    private val inputState: Int,
    private val expectedOutput: GoProBleConnectionStatus
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with inputState={0}, expectedOutput={1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(BluetoothProfile.STATE_DISCONNECTED, GoProBleConnectionStatus.STATE_DISCONNECTED),
                arrayOf(BluetoothProfile.STATE_CONNECTING, GoProBleConnectionStatus.STATE_CONNECTING),
                arrayOf(BluetoothProfile.STATE_CONNECTED, GoProBleConnectionStatus.STATE_CONNECTED),
                arrayOf(BluetoothProfile.STATE_DISCONNECTING, GoProBleConnectionStatus.STATE_DISCONNECTING),
                arrayOf(-1, GoProBleConnectionStatus.UNKNOWN) // Testing an unknown state
            )
        }
    }

    @Test
    fun testGoProBleConnectionStateMapper() {
        val result = goProBleConnectionStateMapper(inputState)
        assertEquals(expectedOutput, result)
    }
}
