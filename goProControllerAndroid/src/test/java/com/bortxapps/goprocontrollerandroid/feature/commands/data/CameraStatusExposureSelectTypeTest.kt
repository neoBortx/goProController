package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusExposureSelectTypeTest(
    private val data: Pair<UByteArray, CameraStatus.LiveViewExposureSelectMode>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.LiveViewExposureSelectMode>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.LiveViewExposureSelectMode.DISABLED),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.LiveViewExposureSelectMode.AUTO),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.LiveViewExposureSelectMode.ISO_LOCK),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.LiveViewExposureSelectMode.HEMISPHERE)
            )
        }
    }

    @Test
    fun testExposureSelectTypeMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.EXPOSURE_SELECT_TYPE.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.EXPOSURE_SELECT_TYPE.name])
    }
}
