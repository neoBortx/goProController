package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapPresetsTest(
    private val pair: Pair<Byte, Presets>
) {
    @Test
    fun `mapPresets should map UByte to Presets`() {
        val dataToMap = pair.first
        val expectedPreset= pair.second
        val actualPreset = mapPresets(dataToMap)
        assertEquals(expectedPreset, actualPreset)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with data={0}, expected={1}")
        fun data(): List<Pair<Byte, Presets>> {
            return listOf(
                Pair((GoProBleCommands.SetPresetsPhoto.byteArray.last()), Presets.PHOTO),
                Pair((GoProBleCommands.SetPresetsVideo.byteArray.last()), Presets.VIDEO),
                Pair((GoProBleCommands.SetPresetsTimeLapse.byteArray.last()), Presets.TIME_LAPSE)
            )
        }
    }
}
