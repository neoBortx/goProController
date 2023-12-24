package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusStateScanWifiTest(
    private val data: Pair<UByteArray, CameraStatus.CurrentScanState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.CurrentScanState>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.CurrentScanState.NEVER_STARTED),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.CurrentScanState.STARTED),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.CurrentScanState.ABORTED),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.CurrentScanState.CANCELED),
                Pair(ubyteArrayOf(4.toUByte()), CameraStatus.CurrentScanState.COMPLETED)
            )
        }
    }

    @Test
    fun testStateScanWifiMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.STATE_SCAN_WIFI.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.STATE_SCAN_WIFI.name])
    }
}
