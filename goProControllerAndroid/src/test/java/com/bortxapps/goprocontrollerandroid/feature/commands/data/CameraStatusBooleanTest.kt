package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CameraStatusBooleanTest(
    private val statusId: CameraStatus.CameraStatusIds,
    private val expectedValue: Boolean
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with statusId={0}, expectedValue={1}")
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(CameraStatus.CameraStatusIds.INTERNAL_BATTERY_PRESENT, true),
                arrayOf(CameraStatus.CameraStatusIds.SYSTEM_HOT, true),
                arrayOf(CameraStatus.CameraStatusIds.SYSTEM_BUSY, true),
                arrayOf(CameraStatus.CameraStatusIds.QUICK_CAPTURE_ACTIVE, true),
                arrayOf(CameraStatus.CameraStatusIds.ENCODING_ACTIVE, true),
                arrayOf(CameraStatus.CameraStatusIds.LCD_LOCK_ACTIVE, true),
                arrayOf(CameraStatus.CameraStatusIds.ENABLE, true),
                arrayOf(CameraStatus.CameraStatusIds.REMOTE_CONTROL_CONNECTED, true),
                arrayOf(CameraStatus.CameraStatusIds.ENABLE_PREVIEW_STREAM, true),
                arrayOf(CameraStatus.CameraStatusIds.DOWNLOAD_CANCEL_REQUEST_PENDING, true),
                arrayOf(CameraStatus.CameraStatusIds.CAMERA_LOCATE_ACTIVE, true),
                arrayOf(CameraStatus.CameraStatusIds.SUPPORTED_PREVIEW_STREAM, true),
                arrayOf(CameraStatus.CameraStatusIds.GPS_STATUS, true),
                arrayOf(CameraStatus.CameraStatusIds.AP_STATE, true),
                arrayOf(CameraStatus.CameraStatusIds.DIGITAL_ZOOM_ACTIVE, true),
                arrayOf(CameraStatus.CameraStatusIds.MOBILE_FRIENDLY_VIDEO, true),
                arrayOf(CameraStatus.CameraStatusIds.FIRST_TIME_USE, true),
                arrayOf(CameraStatus.CameraStatusIds.BAND_5GHZ_AVAIL, true),
                arrayOf(CameraStatus.CameraStatusIds.SYSTEM_READY, true),
                arrayOf(CameraStatus.CameraStatusIds.BATT_OKAY_FOR_OTA, true),
                arrayOf(CameraStatus.CameraStatusIds.VIDEO_LOW_TEMP_ALERT, true),
                arrayOf(CameraStatus.CameraStatusIds.ZOOM_WHILE_ENCODING, true),
                arrayOf(CameraStatus.CameraStatusIds.CAPTURE_DELAY_ACTIVE, true),
                arrayOf(CameraStatus.CameraStatusIds.SCHEDULED_ENABLED, true),
                arrayOf(CameraStatus.CameraStatusIds.SD_RATING_CHECK_ERROR, true),
                arrayOf(CameraStatus.CameraStatusIds.TURBO_TRANSFER, true),
                arrayOf(CameraStatus.CameraStatusIds.USB_CONNECTED, true),
            )
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    @Test
    fun testBooleanStatusDecoding() {
        val mockData = mapOf(
            statusId.id.toUByte() to ubyteArrayOf((if (expectedValue) 1 else 0).toUByte())
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expectedValue.toString(), result[statusId.name])
    }
}

