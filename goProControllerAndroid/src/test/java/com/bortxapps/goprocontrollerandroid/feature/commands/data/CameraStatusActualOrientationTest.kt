package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusActualOrientationTest(
    private val data: Pair<ByteArray, CameraStatus.CameraRotationalOrientation>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.CameraRotationalOrientation>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.CameraRotationalOrientation.DEGREES_0),
                Pair(byteArrayOf(1), CameraStatus.CameraRotationalOrientation.DEGREES_180),
                Pair(byteArrayOf(2), CameraStatus.CameraRotationalOrientation.DEGREES_90),
                Pair(byteArrayOf(3), CameraStatus.CameraRotationalOrientation.DEGREES_270)
            )
        }
    }

    @Test
    fun testActualOrientationMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.ACTUAL_ORIENTATION.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.ACTUAL_ORIENTATION.name])
    }
}
