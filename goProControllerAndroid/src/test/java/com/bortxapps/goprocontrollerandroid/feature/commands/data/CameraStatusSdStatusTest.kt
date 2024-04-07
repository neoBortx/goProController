package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusSdStatusTest(
    private val data: Pair<ByteArray, CameraStatus.PrimaryStorageStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.PrimaryStorageStatus>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.PrimaryStorageStatus.OK),
                Pair(byteArrayOf(1), CameraStatus.PrimaryStorageStatus.SD_CARD_FULL),
                Pair(byteArrayOf(2), CameraStatus.PrimaryStorageStatus.SD_CARD_REMOVED),
                Pair(byteArrayOf(3), CameraStatus.PrimaryStorageStatus.SD_CARD_FORMAT_ERROR),
                Pair(byteArrayOf(4), CameraStatus.PrimaryStorageStatus.SD_CARD_BUSY),
                Pair(byteArrayOf(8), CameraStatus.PrimaryStorageStatus.SD_CARD_SWAPPED)
            )
        }
    }

    @Test
    fun testSdStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.SD_STATUS.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.SD_STATUS.name])
    }
}
