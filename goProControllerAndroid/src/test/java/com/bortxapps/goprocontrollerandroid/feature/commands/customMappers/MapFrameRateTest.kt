package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapFrameRateTest(
    private val pair: Pair<UByte, FrameRate>
) {
    @Test
    fun `mapFrameRate should map UByte to FrameRate`() {
        val dataToMap = pair.first
        val expectedFrameRate = pair.second
        val actualFrameRate = mapFrameRate(dataToMap)
        assertEquals(expectedFrameRate, actualFrameRate)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with data={0}, expected={1}")
        fun data(): List<Pair<UByte, FrameRate>> {
            return listOf(
                Pair((GoProBleCommands.SetFrameRate240.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_240),
                Pair((GoProBleCommands.SetFrameRate200.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_200),
                Pair((GoProBleCommands.SetFrameRate120.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_120),
                Pair((GoProBleCommands.SetFrameRate100.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_100),
                Pair((GoProBleCommands.SetFrameRate60.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_60),
                Pair((GoProBleCommands.SetFrameRate50.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_50),
                Pair((GoProBleCommands.SetFrameRate30.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_30),
                Pair((GoProBleCommands.SetFrameRate25.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_25),
                Pair((GoProBleCommands.SetFrameRate24.byteArray.last()).toUByte(), FrameRate.FRAME_RATE_24)
            )
        }
    }
}
