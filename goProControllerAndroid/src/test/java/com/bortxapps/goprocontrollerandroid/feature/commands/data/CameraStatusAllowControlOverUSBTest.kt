package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusAllowControlOverUSBTest(
    private val data: Pair<UByteArray, CameraStatus.CameraControlOverUSBState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.CameraControlOverUSBState>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.CameraControlOverUSBState.DISABLED),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.CameraControlOverUSBState.ENABLED)
            )
        }
    }

    @Test
    fun testAllowControlOverUSBMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.ALLOW_CONTROL_OVER_USB.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.ALLOW_CONTROL_OVER_USB.name])
    }
}
