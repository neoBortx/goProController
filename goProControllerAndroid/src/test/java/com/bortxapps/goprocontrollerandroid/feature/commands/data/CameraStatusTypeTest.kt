package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusTypeTest(
    private val data: Pair<ByteArray, CameraStatus.LastTypePairing>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.LastTypePairing>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.LastTypePairing.NEVER_PAIRED),
                Pair(byteArrayOf(1), CameraStatus.LastTypePairing.PAIRING_APP),
                Pair(byteArrayOf(2), CameraStatus.LastTypePairing.PAIRING_REMOTE_CONTROL),
                Pair(byteArrayOf(3), CameraStatus.LastTypePairing.PAIRING_BLUETOOTH_DEVICE)
            )
        }
    }

    @Test
    fun testTypeMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.TYPE.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.TYPE.name])
    }
}
