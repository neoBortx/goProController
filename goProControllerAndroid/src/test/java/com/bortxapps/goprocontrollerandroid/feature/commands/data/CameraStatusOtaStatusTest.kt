package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusOtaStatusTest(
    private val data: Pair<UByteArray, CameraStatus.OTAUpdateStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.OTAUpdateStatus>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.OTAUpdateStatus.IDLE),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.OTAUpdateStatus.DOWNLOADING),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.OTAUpdateStatus.VERIFYING),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.OTAUpdateStatus.DOWNLOAD_FAILED),
                Pair(ubyteArrayOf(4.toUByte()), CameraStatus.OTAUpdateStatus.VERIFY_FAILED),
                Pair(ubyteArrayOf(5.toUByte()), CameraStatus.OTAUpdateStatus.READY),
                Pair(ubyteArrayOf(6.toUByte()), CameraStatus.OTAUpdateStatus.GOPRO_APP_DOWNLOADING),
                Pair(ubyteArrayOf(7.toUByte()), CameraStatus.OTAUpdateStatus.GOPRO_APP_VERIFYING),
                Pair(ubyteArrayOf(8.toUByte()), CameraStatus.OTAUpdateStatus.GOPRO_APP_DOWNLOAD_FAILED),
                Pair(ubyteArrayOf(9.toUByte()), CameraStatus.OTAUpdateStatus.GOPRO_APP_VERIFY_FAILED),
                Pair(ubyteArrayOf(10.toUByte()), CameraStatus.OTAUpdateStatus.GOPRO_APP_READY)
            )
        }
    }

    @Test
    fun testOtaStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.OTA_STATUS.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.OTA_STATUS.name])
    }
}
