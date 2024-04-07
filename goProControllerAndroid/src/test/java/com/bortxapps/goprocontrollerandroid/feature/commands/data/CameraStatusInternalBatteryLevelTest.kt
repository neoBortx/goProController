package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusInternalBatteryLevelTest(
    private val data: Pair<ByteArray, CameraStatus.InternalBatteryLevel>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.InternalBatteryLevel>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.InternalBatteryLevel.ZERO),
                Pair(byteArrayOf(1), CameraStatus.InternalBatteryLevel.ONE),
                Pair(byteArrayOf(2), CameraStatus.InternalBatteryLevel.TWO),
                Pair(byteArrayOf(3), CameraStatus.InternalBatteryLevel.THREE)
            )
        }
    }

    @Test
    fun testInternalBatteryLevelMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.INTERNAL_BATTERY_LEVEL.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.INTERNAL_BATTERY_LEVEL.name])
    }

}
