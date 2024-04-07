package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusProvisionStatusTest(
    private val data: Pair<ByteArray, CameraStatus.WiFiAPProvisioningState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.WiFiAPProvisioningState>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.WiFiAPProvisioningState.NEVER_STARTED),
                Pair(byteArrayOf(1), CameraStatus.WiFiAPProvisioningState.STARTED),
                Pair(byteArrayOf(2), CameraStatus.WiFiAPProvisioningState.ABORTED),
                Pair(byteArrayOf(3), CameraStatus.WiFiAPProvisioningState.CANCELED),
                Pair(byteArrayOf(4), CameraStatus.WiFiAPProvisioningState.COMPLETED)
            )
        }
    }

    @Test
    fun testProvisionStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.PROVISION_STATUS.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.PROVISION_STATUS.name])
    }
}
