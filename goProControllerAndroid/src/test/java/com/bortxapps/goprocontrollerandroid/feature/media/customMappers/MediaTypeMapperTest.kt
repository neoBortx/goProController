package com.bortxapps.goprocontrollerandroid.feature.media.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItemType
import com.bortxapps.goprocontrollerandroid.feature.media.data.ContentType
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters

@RunWith(Parameterized::class)
class MediaTypeMapperTest(
    private val contentType: ContentType?,
    private val expected: GoProMediaItemType
) {

    @Test
    fun `mapMediaType should map ContentType to GoProMediaItemType`() {
        val result = mapMediaType(contentType)
        assertEquals(expected, result)
    }

    companion object {
        @JvmStatic
        @Parameters
        fun data(): List<Array<out Enum<*>?>> {
            return listOf(
                arrayOf(ContentType.VIDEO, GoProMediaItemType.VIDEO),
                arrayOf(ContentType.LOOPING, GoProMediaItemType.LOOPING),
                arrayOf(ContentType.CHAPTERED_VIDEO, GoProMediaItemType.CHAPTERED_VIDEO),
                arrayOf(ContentType.TIME_LAPSE, GoProMediaItemType.TIMELAPSE),
                arrayOf(ContentType.SINGLE_PHOTO, GoProMediaItemType.SINGLE_PHOTO),
                arrayOf(ContentType.BURST_PHOTO, GoProMediaItemType.BURST_PHOTO),
                arrayOf(ContentType.TIME_LAPSE_PHOTO, GoProMediaItemType.TIMELAPSE_PHOTO),
                arrayOf(ContentType.NIGHT_LAPSE_PHOTO, GoProMediaItemType.NIGHT_LAPSE_PHOTO),
                arrayOf(ContentType.NIGHT_PHOTO, GoProMediaItemType.NIGHT_PHOTO),
                arrayOf(ContentType.CONTINUOUS_PHOTO, GoProMediaItemType.CONTINUOUS_PHOTO),
                arrayOf(ContentType.RAW_PHOTO, GoProMediaItemType.RAW_PHOTO),
                arrayOf(ContentType.LIVE_BURST, GoProMediaItemType.LIVE_BURST),
                arrayOf(null, GoProMediaItemType.UNKNOWN)
            )
        }
    }
}