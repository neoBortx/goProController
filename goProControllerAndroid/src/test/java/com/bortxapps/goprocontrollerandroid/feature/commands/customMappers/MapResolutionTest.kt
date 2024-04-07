package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapResolutionTest(
    private val pair: Pair<Byte, Resolution>
) {
    @Test
    fun `mapResolution should map UByte to Resolution`() {
        val dataToMap = pair.first
        val expectedResolution = pair.second
        val actualResolution = mapResolution(dataToMap)
        assertEquals(expectedResolution, actualResolution)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with data={0}, expected={1}")
        fun data(): List<Pair<Byte, Resolution>> {
            return listOf(
                Pair((GoProBleCommands.SetResolution53K.byteArray.last()), Resolution.RESOLUTION_5_3K),
                Pair((GoProBleCommands.SetResolution4K.byteArray.last()), Resolution.RESOLUTION_4K),
                Pair((GoProBleCommands.SetResolution4K43.byteArray.last()), Resolution.RESOLUTION_4K_4_3),
                Pair((GoProBleCommands.SetResolution2K.byteArray.last()), Resolution.RESOLUTION_2_7K),
                Pair((GoProBleCommands.SetResolution2K43.byteArray.last()), Resolution.RESOLUTION_2_7K_4_3),
                Pair((GoProBleCommands.SetResolution1440.byteArray.last()), Resolution.RESOLUTION_1440),
                Pair((GoProBleCommands.SetResolution1080.byteArray.last()), Resolution.RESOLUTION_1080),
                Pair((GoProBleCommands.SetResolution5KGoPro12.byteArray.last()), Resolution.RESOLUTION_5K_GOPRO_12),
                Pair((GoProBleCommands.SetResolution4KGoPro12.byteArray.last()), Resolution.RESOLUTION_4K_GOPRO_12),
                Pair((GoProBleCommands.SetResolution4K43GoPro12.byteArray.last()), Resolution.RESOLUTION_4K_4_3_GOPRO_12),
                Pair((GoProBleCommands.SetResolution2KGoPro12.byteArray.last()), Resolution.RESOLUTION_2K_GO_PRO_12),
                Pair((GoProBleCommands.SetResolution2K43GoPro12.byteArray.last()), Resolution.RESOLUTION_2K_4_3_GO_PRO_12),
                Pair((GoProBleCommands.SetResolution1080GoPro12.byteArray.last()), Resolution.RESOLUTION_1080_GOPRO_12)
            )
        }
    }
}
