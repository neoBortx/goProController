package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusCameraLensTypeTest(
    private val data: Pair<UByteArray, CameraStatus.CameraLensType>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.CameraLensType>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.CameraLensType.DEFAULT),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.CameraLensType.MAX_LENS),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.CameraLensType.MAX_LENS_2_0)
            )
        }
    }

    @Test
    fun testCameraLensTypeMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.CAMERA_LENS_TYPE.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.CAMERA_LENS_TYPE.name])
    }
}
