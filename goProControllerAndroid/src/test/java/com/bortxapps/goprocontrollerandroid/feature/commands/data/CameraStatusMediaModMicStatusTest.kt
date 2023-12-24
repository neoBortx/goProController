package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusMediaModMicStatusTest(
    private val data: Pair<UByteArray, CameraStatus.MediaModState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.MediaModState>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.MediaModState.MICROPHONE_REMOVED),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.MediaModState.MICROPHONE_ONLY),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.MediaModState.MICROPHONE_WITH_EXTERNAL_MIC)
            )
        }
    }

    @Test
    fun testMediaModMicStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.MEDIA_MOD_MIC_STATUS.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.MEDIA_MOD_MIC_STATUS.name])
    }
}
