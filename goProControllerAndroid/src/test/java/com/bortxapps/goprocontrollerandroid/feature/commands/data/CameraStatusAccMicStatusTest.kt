package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CameraStatusAccMicStatusTest(
    private val data: Pair<ByteArray, CameraStatus.MicrophoneAccessoryStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.MicrophoneAccessoryStatus>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.MicrophoneAccessoryStatus.NOT_CONNECTED),
                Pair(byteArrayOf(1), CameraStatus.MicrophoneAccessoryStatus.CONNECTED),
                Pair(byteArrayOf(2), CameraStatus.MicrophoneAccessoryStatus.CONNECTED_WITH_MICROPHONE)
            )
        }
    }

    @Test
    fun testAccMicStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.ACC_MIC_STATUS.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.ACC_MIC_STATUS.name])
    }


}
