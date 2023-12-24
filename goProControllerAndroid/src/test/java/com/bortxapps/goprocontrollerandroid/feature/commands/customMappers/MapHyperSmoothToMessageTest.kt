package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapHyperSmoothToMessageTest(
    private val hyperSmooth: HyperSmooth,
    private val expectedByteArray: ByteArray
) {
    @Test
    fun `mapHyperSmoothToMessage should map HyperSmooth to ByteArray`() {
        val actualByteArray = mapHyperSmoothToMessage(hyperSmooth)
        assertEquals(expectedByteArray, actualByteArray)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with HyperSmooth={0}, expected ByteArray={1}")
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(HyperSmooth.OFF, GoProBleCommands.SetHyperSmoothOff.byteArray),
                arrayOf(HyperSmooth.LOW, GoProBleCommands.SetHyperSmoothLow.byteArray),
                arrayOf(HyperSmooth.HIGH, GoProBleCommands.SetHyperSmoothHigh.byteArray),
                arrayOf(HyperSmooth.BOOST, GoProBleCommands.SetHyperSmoothBoost.byteArray),
                arrayOf(HyperSmooth.AUTO, GoProBleCommands.SetHyperSmoothAuto.byteArray),
                arrayOf(HyperSmooth.STANDARD, GoProBleCommands.SetHyperSmoothStandard.byteArray)
            )
        }
    }
}
