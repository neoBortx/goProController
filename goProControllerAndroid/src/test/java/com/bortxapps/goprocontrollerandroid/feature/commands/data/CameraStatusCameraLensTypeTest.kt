package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusCameraLensTypeTest(
    private val data: Pair<ByteArray, CameraStatus.CameraLensType>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.CameraLensType>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.CameraLensType.DEFAULT),
                Pair(byteArrayOf(1), CameraStatus.CameraLensType.MAX_LENS),
                Pair(byteArrayOf(2), CameraStatus.CameraLensType.MAX_LENS_2_0)
            )
        }
    }

    @Test
    fun testCameraLensTypeMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.CAMERA_LENS_TYPE.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.CAMERA_LENS_TYPE.name])
    }
}
