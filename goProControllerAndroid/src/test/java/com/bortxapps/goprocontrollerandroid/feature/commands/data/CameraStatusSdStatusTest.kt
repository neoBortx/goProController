package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusSdStatusTest(
    private val data: Pair<UByteArray, CameraStatus.PrimaryStorageStatus>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.PrimaryStorageStatus>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.PrimaryStorageStatus.OK),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.PrimaryStorageStatus.SD_CARD_FULL),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.PrimaryStorageStatus.SD_CARD_REMOVED),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.PrimaryStorageStatus.SD_CARD_FORMAT_ERROR),
                Pair(ubyteArrayOf(4.toUByte()), CameraStatus.PrimaryStorageStatus.SD_CARD_BUSY),
                Pair(ubyteArrayOf(8.toUByte()), CameraStatus.PrimaryStorageStatus.SD_CARD_SWAPPED)
            )
        }
    }

    @Test
    fun testSdStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.SD_STATUS.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.SD_STATUS.name])
    }
}
