package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.nio.ByteBuffer

@RunWith(Parameterized::class)
class CameraStatusIntTest(
    private val statusId: CameraStatus.CameraStatusIds,
    private val expectedValue: Int
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with statusId={0}, expectedValue={1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(CameraStatus.CameraStatusIds.VIDEO_PROGRESS_COUNTER, 123),
                arrayOf(CameraStatus.CameraStatusIds.PAIR_TIME, 456),
                arrayOf(CameraStatus.CameraStatusIds.SCAN_TIME_MSEC, 789),
                arrayOf(CameraStatus.CameraStatusIds.REMOTE_CONTROL_VERSION, 101112),
                arrayOf(CameraStatus.CameraStatusIds.APP_COUNT, 131415),
                arrayOf(CameraStatus.CameraStatusIds.REMAINING_PHOTOS, 161718),
                arrayOf(CameraStatus.CameraStatusIds.REMAINING_VIDEO_TIME, 192021),
                arrayOf(CameraStatus.CameraStatusIds.NUM_GROUP_PHOTOS, 222324),
                arrayOf(CameraStatus.CameraStatusIds.NUM_GROUP_VIDEOS, 252627),
                arrayOf(CameraStatus.CameraStatusIds.NUM_TOTAL_PHOTOS, 282930),
                arrayOf(CameraStatus.CameraStatusIds.NUM_TOTAL_VIDEOS, 313233),
                arrayOf(CameraStatus.CameraStatusIds.REMAINING_SPACE_KB, 343536),
                arrayOf(CameraStatus.CameraStatusIds.LAST_HILIGHT_TIME_MSEC, 373839),
                arrayOf(CameraStatus.CameraStatusIds.NEXT_POLL_MSEC, 404142),
                arrayOf(CameraStatus.CameraStatusIds.REMAINING_TIMELAPSE_TIME, 434445),
                arrayOf(CameraStatus.CameraStatusIds.EXPOSURE_SELECT_X, 464748),
                arrayOf(CameraStatus.CameraStatusIds.EXPOSURE_SELECT_Y, 495051)
            )
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testIntegerStatusDecoding() {
        val mockData = mapOf(
            statusId.id.toByte() to ByteBuffer.allocate(4).putInt(expectedValue).array()
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expectedValue.toString(), result[statusId.name])
    }
}
