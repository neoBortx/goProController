package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusMediaModStatusTest(
    private val data: Pair<ByteArray, CameraStatus.MediaModeStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.MediaModeStatus>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.MediaModeStatus.SELFIE_MODE_0_HDMI_0_MEDIA_MOD_FALSE),
                Pair(byteArrayOf(1), CameraStatus.MediaModeStatus.SELFIE_MODE_0_HDMI_0_MEDIA_MOD_TRUE),
                // Add other values as necessary
            )
        }
    }

    @Test
    fun testMediaModStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.MEDIA_MOD_STATUS.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.MEDIA_MOD_STATUS.name])
    }
}
