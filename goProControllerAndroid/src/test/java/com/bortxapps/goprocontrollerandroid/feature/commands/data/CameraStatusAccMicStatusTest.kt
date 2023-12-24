package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusAccMicStatusTest(
    private val data: Pair<UByteArray, CameraStatus.MicrophoneAccessoryStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.MicrophoneAccessoryStatus>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.MicrophoneAccessoryStatus.NOT_CONNECTED),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.MicrophoneAccessoryStatus.CONNECTED),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.MicrophoneAccessoryStatus.CONNECTED_WITH_MICROPHONE)
            )
        }
    }

    @Test
    fun testAccMicStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.ACC_MIC_STATUS.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.ACC_MIC_STATUS.name])
    }


}
