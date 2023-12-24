package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapFrameRateToMessageTest(
    private val frameRate: FrameRate,
    private val expectedByteArray: ByteArray
) {
    @Test
    fun `mapFrameRateToMessage should map FrameRate to ByteArray`() {
        val actualByteArray = mapFrameRateToMessage(frameRate)
        assertEquals(expectedByteArray, actualByteArray)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with FrameRate={0}, expected ByteArray={1}")
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(FrameRate.FRAME_RATE_240, GoProBleCommands.SetFrameRate240.byteArray),
                arrayOf(FrameRate.FRAME_RATE_200, GoProBleCommands.SetFrameRate200.byteArray),
                arrayOf(FrameRate.FRAME_RATE_120, GoProBleCommands.SetFrameRate120.byteArray),
                arrayOf(FrameRate.FRAME_RATE_100, GoProBleCommands.SetFrameRate100.byteArray),
                arrayOf(FrameRate.FRAME_RATE_60, GoProBleCommands.SetFrameRate60.byteArray),
                arrayOf(FrameRate.FRAME_RATE_50, GoProBleCommands.SetFrameRate50.byteArray),
                arrayOf(FrameRate.FRAME_RATE_30, GoProBleCommands.SetFrameRate30.byteArray),
                arrayOf(FrameRate.FRAME_RATE_25, GoProBleCommands.SetFrameRate25.byteArray),
                arrayOf(FrameRate.FRAME_RATE_24, GoProBleCommands.SetFrameRate24.byteArray)

            )
        }
    }
}
