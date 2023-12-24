package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusWirelessBandTest(
    private val data: Pair<UByteArray, CameraStatus.WirelessBand>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.WirelessBand>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.WirelessBand.BAND_2_4_GHZ),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.WirelessBand.BAND_5_GHZ),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.WirelessBand.MAX)
            )
        }
    }

    @Test
    fun testWirelessBandMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.WIRELESS_BAND.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.WIRELESS_BAND.name])
    }
}