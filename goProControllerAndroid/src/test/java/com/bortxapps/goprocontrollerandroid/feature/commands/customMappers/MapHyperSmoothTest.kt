package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapHyperSmoothTest(
    private val pair: Pair<UByte, HyperSmooth>
) {
    @Test
    fun `mapHyperSmooth should map UByte to HyperSmooth`() {
        val dataToMap = pair.first
        val expectedHyperSmooth = pair.second
        val actualHyperSmooth = mapHyperSmooth(dataToMap)
        assertEquals(expectedHyperSmooth, actualHyperSmooth)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with data={0}, expected={1}")
        fun data(): List<Pair<UByte, HyperSmooth>> {
            return listOf(
                Pair((GoProBleCommands.SetHyperSmoothOff.byteArray.last()).toUByte(), HyperSmooth.OFF),
                Pair((GoProBleCommands.SetHyperSmoothLow.byteArray.last()).toUByte(), HyperSmooth.LOW),
                Pair((GoProBleCommands.SetHyperSmoothHigh.byteArray.last()).toUByte(), HyperSmooth.HIGH),
                Pair((GoProBleCommands.SetHyperSmoothBoost.byteArray.last()).toUByte(), HyperSmooth.BOOST),
                Pair((GoProBleCommands.SetHyperSmoothAuto.byteArray.last()).toUByte(), HyperSmooth.AUTO),
                Pair((GoProBleCommands.SetHyperSmoothStandard.byteArray.last()).toUByte(), HyperSmooth.STANDARD)
            )
        }
    }
}
