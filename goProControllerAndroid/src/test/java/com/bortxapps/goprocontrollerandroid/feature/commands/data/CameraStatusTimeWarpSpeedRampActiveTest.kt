package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusTimeWarpSpeedRampActiveTest(
    private val data: Pair<ByteArray, CameraStatus.TimeWarpSpeed>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.TimeWarpSpeed>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.TimeWarpSpeed.SPEED_15X),
                Pair(byteArrayOf(1), CameraStatus.TimeWarpSpeed.SPEED_30X),
                Pair(byteArrayOf(2), CameraStatus.TimeWarpSpeed.SPEED_60X),
                Pair(byteArrayOf(3), CameraStatus.TimeWarpSpeed.SPEED_150X),
                Pair(byteArrayOf(4), CameraStatus.TimeWarpSpeed.SPEED_300X),
                // Add more values as needed
            )
        }
    }

    @Test
    fun testTimeWarpSpeedRampActiveMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.TIMEWARP_SPEED_RAMP_ACTIVE.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.TIMEWARP_SPEED_RAMP_ACTIVE.name])
    }
}
