package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class MapPresetsTest(
    private val pair: Pair<UByte, Presets>
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
        fun data(): List<Pair<UByte, Presets>> {
            return listOf(
                Pair((GoProBleCommands.SetPresetsPhoto.byteArray.last()).toUByte(), Presets.PHOTO),
                Pair((GoProBleCommands.SetPresetsVideo.byteArray.last()).toUByte(), Presets.VIDEO),
                Pair((GoProBleCommands.SetPresetsTimeLapse.byteArray.last()).toUByte(), Presets.TIME_LAPSE)
            )
        }
    }
}
