package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusCameraControlStatusTest(
    private val data: Pair<ByteArray, CameraStatus.CameraControlStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.CameraControlStatus>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.CameraControlStatus.CAMERA_IDLE),
                Pair(byteArrayOf(1), CameraStatus.CameraControlStatus.CAMERA_CONTROL),
                Pair(byteArrayOf(2), CameraStatus.CameraControlStatus.CAMERA_EXTERNAL_CONTROL)
            )
        }
    }

    @Test
    fun testCameraControlStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.CAMERA_CONTROL_STATUS.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.CAMERA_CONTROL_STATUS.name])
    }
}
