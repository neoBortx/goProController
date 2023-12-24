package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusTypeTest(
    private val data: Pair<UByteArray, CameraStatus.LastTypePairing>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.LastTypePairing>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.LastTypePairing.NEVER_PAIRED),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.LastTypePairing.PAIRING_APP),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.LastTypePairing.PAIRING_REMOTE_CONTROL),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.LastTypePairing.PAIRING_BLUETOOTH_DEVICE)
            )
        }
    }

    @Test
    fun testTypeMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.TYPE.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.TYPE.name])
    }
}
