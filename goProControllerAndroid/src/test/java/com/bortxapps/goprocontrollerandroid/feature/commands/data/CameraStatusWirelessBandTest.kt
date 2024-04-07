package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusWirelessBandTest(
    private val data: Pair<ByteArray, CameraStatus.WirelessBand>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.WirelessBand>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.WirelessBand.BAND_2_4_GHZ),
                Pair(byteArrayOf(1), CameraStatus.WirelessBand.BAND_5_GHZ),
                Pair(byteArrayOf(2), CameraStatus.WirelessBand.MAX)
            )
        }
    }

    @Test
    fun testWirelessBandMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.WIRELESS_BAND.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.WIRELESS_BAND.name])
    }
}