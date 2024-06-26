package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

@OptIn(ExperimentalUnsignedTypes::class)
class CameraStatusTest {

    //Just for test true / false converter
    @Test
    fun `decodeStatus should correctly decode INTERNAL_BATTERY_PRESENT status`() {
        // Arrange
        // Assuming '1' represents the internal battery is present and '0' represents it's not
        val mockDataBatteryPresent = mapOf(
            CameraStatus.CameraStatusIds.INTERNAL_BATTERY_PRESENT.id.toByte() to byteArrayOf(1)
        )
        val mockDataBatteryAbsent = mapOf(
            CameraStatus.CameraStatusIds.INTERNAL_BATTERY_PRESENT.id.toByte() to byteArrayOf(0)
        )

        // Act
        val resultBatteryPresent = CameraStatus.decodeStatus(mockDataBatteryPresent)
        val resultBatteryAbsent = CameraStatus.decodeStatus(mockDataBatteryAbsent)

        // Assert
        assertEquals("true", resultBatteryPresent[CameraStatus.CameraStatusIds.INTERNAL_BATTERY_PRESENT.name])
        assertEquals("false", resultBatteryAbsent[CameraStatus.CameraStatusIds.INTERNAL_BATTERY_PRESENT.name])
    }
    //endregion


    //region INTERNAL_BATTERY_LEVEL
    @Test
    fun `decodeStatus should correctly decode all INTERNAL_BATTERY_LEVEL values`() {
        val batteryLevelData = mapOf(
            CameraStatus.InternalBatteryLevel.ZERO to byteArrayOf(0),
            CameraStatus.InternalBatteryLevel.ONE to byteArrayOf(1),
            CameraStatus.InternalBatteryLevel.TWO to byteArrayOf(2),
            CameraStatus.InternalBatteryLevel.THREE to byteArrayOf(3)
        )

        batteryLevelData.forEach { (expectedLevel, byteArray) ->
            val mockData = mapOf(
                CameraStatus.CameraStatusIds.INTERNAL_BATTERY_LEVEL.id.toByte() to byteArray
            )

            val result = CameraStatus.decodeStatus(mockData)

            assertEquals(expectedLevel.toString(), result[CameraStatus.CameraStatusIds.INTERNAL_BATTERY_LEVEL.name])
        }
    }

    @Test
    fun `test decode INTERNAL_BATTERY_LEVEL with an unknown value throws IllegalArgumentException`() {
        val mockData = mapOf(
            CameraStatus.CameraStatusIds.INTERNAL_BATTERY_LEVEL.id.toByte() to ByteArray(1) { 56.toByte() }
        )

        assertThrows(IllegalArgumentException::class.java) {
            CameraStatus.decodeStatus(mockData)
        }
    }
    //endregion


    //region SYSTEM_HOT
    @Test
    fun `decodeStatus should correctly decode SYSTEM_HOT status`() {
        val systemHotData = mapOf(
            true to byteArrayOf(1), // Assuming '1' represents system is hot
            false to byteArrayOf(0) // Assuming '0' represents system is not hot
        )

        systemHotData.forEach { (expectedState, byteArray) ->
            val mockData = mapOf(
                CameraStatus.CameraStatusIds.SYSTEM_HOT.id.toByte() to byteArray
            )

            val result = CameraStatus.decodeStatus(mockData)

            assertEquals(expectedState.toString(), result[CameraStatus.CameraStatusIds.SYSTEM_HOT.name])
        }
    }
    //endregion

    //region SYSTEM_BUSY
    @Test
    fun `decodeStatus should correctly decode SYSTEM_BUSY status`() {
        val systemBusyData = mapOf(
            true to byteArrayOf(1), // Assuming '1' represents system is busy
            false to byteArrayOf(0) // Assuming '0' represents system is not busy
        )

        systemBusyData.forEach { (expectedState, byteArray) ->
            val mockData = mapOf(
                CameraStatus.CameraStatusIds.SYSTEM_BUSY.id.toByte() to byteArray
            )

            val result = CameraStatus.decodeStatus(mockData)

            assertEquals(expectedState.toString(), result[CameraStatus.CameraStatusIds.SYSTEM_BUSY.name])
        }
    }
    //endregion

    //region QUICK_CAPTURE_ACTIVE
    @Test
    fun `decodeStatus should correctly decode QUICK_CAPTURE_ACTIVE status`() {
        val quickCaptureActiveData = mapOf(
            true to byteArrayOf(1), // Assuming '1' represents Quick Capture is active
            false to byteArrayOf(0) // Assuming '0' represents Quick Capture is not active
        )

        quickCaptureActiveData.forEach { (expectedState, byteArray) ->
            val mockData = mapOf(
                CameraStatus.CameraStatusIds.QUICK_CAPTURE_ACTIVE.id.toByte() to byteArray
            )

            val result = CameraStatus.decodeStatus(mockData)

            assertEquals(expectedState.toString(), result[CameraStatus.CameraStatusIds.QUICK_CAPTURE_ACTIVE.name])
        }
    }
    //endregion

    //region ENCODING_ACTIVE
    @Test
    fun `decodeStatus should correctly decode ENCODING_ACTIVE status`() {
        val encodingActiveData = mapOf(
            true to byteArrayOf(1), // '1' represents encoding is active
            false to byteArrayOf(0) // '0' represents encoding is not active
        )

        encodingActiveData.forEach { (expectedState, byteArray) ->
            val mockData = mapOf(
                CameraStatus.CameraStatusIds.ENCODING_ACTIVE.id.toByte() to byteArray
            )

            val result = CameraStatus.decodeStatus(mockData)

            assertEquals(expectedState.toString(), result[CameraStatus.CameraStatusIds.ENCODING_ACTIVE.name])
        }
    }
    //endregion

    //region LCD_LOCK_ACTIVE
    @Test
    fun `decodeStatus should correctly decode LCD_LOCK_ACTIVE status`() {
        val lcdLockActiveData = mapOf(
            true to byteArrayOf(1), // '1' represents LCD lock is active
            false to byteArrayOf(0) // '0' represents LCD lock is not active
        )

        lcdLockActiveData.forEach { (expectedState, byteArray) ->
            val mockData = mapOf(
                CameraStatus.CameraStatusIds.LCD_LOCK_ACTIVE.id.toByte() to byteArray
            )

            val result = CameraStatus.decodeStatus(mockData)

            assertEquals(expectedState.toString(), result[CameraStatus.CameraStatusIds.LCD_LOCK_ACTIVE.name])
        }
    }
    //endregion

    //region VIDEO_PROGRESS_COUNTER
    @Test
    fun `decodeStatus should correctly decode VIDEO_PROGRESS_COUNTER status`() {
        val videoProgressTime = 45.toByte() // Example non-zero value for video progress (in seconds)
        val mockData = mapOf(
            CameraStatus.CameraStatusIds.VIDEO_PROGRESS_COUNTER.id.toByte() to byteArrayOf(videoProgressTime)
        )

        val result = CameraStatus.decodeStatus(mockData)

        assertEquals(videoProgressTime.toString(), result[CameraStatus.CameraStatusIds.VIDEO_PROGRESS_COUNTER.name])
    }
    //endregion

}
