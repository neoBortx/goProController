package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class CameraStatusStateScanWifiTest(
    private val data: Pair<ByteArray, CameraStatus.CurrentScanState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.CurrentScanState>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.CurrentScanState.NEVER_STARTED),
                Pair(byteArrayOf(1), CameraStatus.CurrentScanState.STARTED),
                Pair(byteArrayOf(2), CameraStatus.CurrentScanState.ABORTED),
                Pair(byteArrayOf(3), CameraStatus.CurrentScanState.CANCELED),
                Pair(byteArrayOf(4), CameraStatus.CurrentScanState.COMPLETED)
            )
        }
    }

    @Test
    fun testStateScanWifiMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.STATE_SCAN_WIFI.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.STATE_SCAN_WIFI.name])
    }
}
