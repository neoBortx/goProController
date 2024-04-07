package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusOtaStatusTest(
    private val data: Pair<ByteArray, CameraStatus.OTAUpdateStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.OTAUpdateStatus>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.OTAUpdateStatus.IDLE),
                Pair(byteArrayOf(1), CameraStatus.OTAUpdateStatus.DOWNLOADING),
                Pair(byteArrayOf(2), CameraStatus.OTAUpdateStatus.VERIFYING),
                Pair(byteArrayOf(3), CameraStatus.OTAUpdateStatus.DOWNLOAD_FAILED),
                Pair(byteArrayOf(4), CameraStatus.OTAUpdateStatus.VERIFY_FAILED),
                Pair(byteArrayOf(5), CameraStatus.OTAUpdateStatus.READY),
                Pair(byteArrayOf(6), CameraStatus.OTAUpdateStatus.GOPRO_APP_DOWNLOADING),
                Pair(byteArrayOf(7), CameraStatus.OTAUpdateStatus.GOPRO_APP_VERIFYING),
                Pair(byteArrayOf(8), CameraStatus.OTAUpdateStatus.GOPRO_APP_DOWNLOAD_FAILED),
                Pair(byteArrayOf(9), CameraStatus.OTAUpdateStatus.GOPRO_APP_VERIFY_FAILED),
                Pair(byteArrayOf(10), CameraStatus.OTAUpdateStatus.GOPRO_APP_READY)
            )
        }
    }

    @Test
    fun testOtaStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.OTA_STATUS.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.OTA_STATUS.name])
    }
}
