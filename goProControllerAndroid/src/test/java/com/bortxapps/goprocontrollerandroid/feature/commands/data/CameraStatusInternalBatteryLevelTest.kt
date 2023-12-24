package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusInternalBatteryLevelTest(
    private val data: Pair<UByteArray, CameraStatus.InternalBatteryLevel>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.InternalBatteryLevel>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.InternalBatteryLevel.ZERO),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.InternalBatteryLevel.ONE),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.InternalBatteryLevel.TWO),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.InternalBatteryLevel.THREE)
            )
        }
    }

    @Test
    fun testInternalBatteryLevelMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.INTERNAL_BATTERY_LEVEL.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.INTERNAL_BATTERY_LEVEL.name])
    }

}
