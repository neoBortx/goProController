package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusCameraControlStatusTest(
    private val data: Pair<UByteArray, CameraStatus.CameraControlStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.CameraControlStatus>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.CameraControlStatus.CAMERA_IDLE),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.CameraControlStatus.CAMERA_CONTROL),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.CameraControlStatus.CAMERA_EXTERNAL_CONTROL)
            )
        }
    }

    @Test
    fun testCameraControlStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.CAMERA_CONTROL_STATUS.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.CAMERA_CONTROL_STATUS.name])
    }
}
