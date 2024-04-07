package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapHyperSmoothTest(
    private val pair: Pair<Byte, HyperSmooth>
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
        fun data(): List<Pair<Byte, HyperSmooth>> {
            return listOf(
                Pair((GoProBleCommands.SetHyperSmoothOff.byteArray.last()), HyperSmooth.OFF),
                Pair((GoProBleCommands.SetHyperSmoothLow.byteArray.last()), HyperSmooth.LOW),
                Pair((GoProBleCommands.SetHyperSmoothHigh.byteArray.last()), HyperSmooth.HIGH),
                Pair((GoProBleCommands.SetHyperSmoothBoost.byteArray.last()), HyperSmooth.BOOST),
                Pair((GoProBleCommands.SetHyperSmoothAuto.byteArray.last()), HyperSmooth.AUTO),
                Pair((GoProBleCommands.SetHyperSmoothStandard.byteArray.last()), HyperSmooth.STANDARD)
            )
        }
    }
}
