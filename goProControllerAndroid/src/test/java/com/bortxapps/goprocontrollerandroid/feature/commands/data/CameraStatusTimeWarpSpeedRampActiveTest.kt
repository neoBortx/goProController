package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusTimeWarpSpeedRampActiveTest(
    private val data: Pair<UByteArray, CameraStatus.TimeWarpSpeed>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.TimeWarpSpeed>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.TimeWarpSpeed.SPEED_15X),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.TimeWarpSpeed.SPEED_30X),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.TimeWarpSpeed.SPEED_60X),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.TimeWarpSpeed.SPEED_150X),
                Pair(ubyteArrayOf(4.toUByte()), CameraStatus.TimeWarpSpeed.SPEED_300X),
                // Add more values as needed
            )
        }
    }

    @Test
    fun testTimeWarpSpeedRampActiveMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.TIMEWARP_SPEED_RAMP_ACTIVE.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.TIMEWARP_SPEED_RAMP_ACTIVE.name])
    }
}
