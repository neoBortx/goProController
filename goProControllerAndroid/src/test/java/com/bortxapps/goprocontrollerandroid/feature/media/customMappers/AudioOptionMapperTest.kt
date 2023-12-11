package com.bortxapps.goprocontrollerandroid.feature.media.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.GoProAudioOption
import com.bortxapps.goprocontrollerandroid.feature.media.data.AudioOption
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class AudioOptionMapperTest(
    private val input: AudioOption?,
    private val expected: GoProAudioOption
) {

    @Test
    fun `mapAudioOption should map AudioOption to GoProAudioOption`() {
        val result = mapAudioOption(input)
        assertEquals(expected, result)
    }

    companion object {
        @JvmStatic
        @Parameterized.Parameters
        fun data(): List<Array<out Enum<*>?>> {
            return listOf(
                arrayOf(AudioOption.OFF, GoProAudioOption.OFF),
                arrayOf(AudioOption.STEREO, GoProAudioOption.STEREO),
                arrayOf(AudioOption.AUTO, GoProAudioOption.AUTO),
                arrayOf(AudioOption.WIND, GoProAudioOption.WIND),
                arrayOf(null, GoProAudioOption.UNKNOWN)
            )
        }
    }
}