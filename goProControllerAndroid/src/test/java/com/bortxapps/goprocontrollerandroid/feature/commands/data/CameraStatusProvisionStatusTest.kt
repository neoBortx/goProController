package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusProvisionStatusTest(
    private val data: Pair<UByteArray, CameraStatus.WiFiAPProvisioningState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.WiFiAPProvisioningState>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.WiFiAPProvisioningState.NEVER_STARTED),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.WiFiAPProvisioningState.STARTED),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.WiFiAPProvisioningState.ABORTED),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.WiFiAPProvisioningState.CANCELED),
                Pair(ubyteArrayOf(4.toUByte()), CameraStatus.WiFiAPProvisioningState.COMPLETED)
            )
        }
    }

    @Test
    fun testProvisionStatusMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.PROVISION_STATUS.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.PROVISION_STATUS.name])
    }
}
