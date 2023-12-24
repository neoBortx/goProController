package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapResolutionToMessageTest(
    private val resolution: Resolution,
    private val expectedByteArray: ByteArray
) {
    @Test
    fun `mapResolutionToMessage should map Resolution to ByteArray`() {
        val actualByteArray = mapResolutionToMessage(resolution)
        assertEquals(expectedByteArray, actualByteArray)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with Resolution={0}, expected ByteArray={1}")
        fun data(): List<Array<Any>> {
            return listOf(
                arrayOf(Resolution.RESOLUTION_5_3K, GoProBleCommands.SetResolution53K.byteArray),
                arrayOf(Resolution.RESOLUTION_4K, GoProBleCommands.SetResolution4K.byteArray),
                arrayOf(Resolution.RESOLUTION_4K_4_3, GoProBleCommands.SetResolution4K43.byteArray),
                arrayOf(Resolution.RESOLUTION_2_7K, GoProBleCommands.SetResolution2K.byteArray),
                arrayOf(Resolution.RESOLUTION_2_7K_4_3, GoProBleCommands.SetResolution2K43.byteArray),
                arrayOf(Resolution.RESOLUTION_1440, GoProBleCommands.SetResolution1440.byteArray),
                arrayOf(Resolution.RESOLUTION_1080, GoProBleCommands.SetResolution1080.byteArray),
                arrayOf(Resolution.RESOLUTION_5K_GOPRO_12, GoProBleCommands.SetResolution5KGoPro12.byteArray),
                arrayOf(Resolution.RESOLUTION_4K_GOPRO_12, GoProBleCommands.SetResolution4KGoPro12.byteArray),
                arrayOf(Resolution.RESOLUTION_4K_4_3_GOPRO_12, GoProBleCommands.SetResolution4K43GoPro12.byteArray),
                arrayOf(Resolution.RESOLUTION_2K_GO_PRO_12, GoProBleCommands.SetResolution2KGoPro12.byteArray),
                arrayOf(Resolution.RESOLUTION_2K_4_3_GO_PRO_12, GoProBleCommands.SetResolution2K43GoPro12.byteArray),
                arrayOf(Resolution.RESOLUTION_1080_GOPRO_12, GoProBleCommands.SetResolution1080GoPro12.byteArray)
            )
        }
    }
}
