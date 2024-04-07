package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusExposureSelectTypeTest(
    private val data: Pair<ByteArray, CameraStatus.LiveViewExposureSelectMode>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.LiveViewExposureSelectMode>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.LiveViewExposureSelectMode.DISABLED),
                Pair(byteArrayOf(1), CameraStatus.LiveViewExposureSelectMode.AUTO),
                Pair(byteArrayOf(2), CameraStatus.LiveViewExposureSelectMode.ISO_LOCK),
                Pair(byteArrayOf(3), CameraStatus.LiveViewExposureSelectMode.HEMISPHERE)
            )
        }
    }

    @Test
    fun testExposureSelectTypeMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.EXPOSURE_SELECT_TYPE.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.EXPOSURE_SELECT_TYPE.name])
    }
}
