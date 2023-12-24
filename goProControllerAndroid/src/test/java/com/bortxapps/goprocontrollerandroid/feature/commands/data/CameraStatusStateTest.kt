package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusStateTest(
    private val data: Pair<UByteArray, CameraStatus.PairingState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<UByteArray, CameraStatus.PairingState>> {
            return listOf(
                Pair(ubyteArrayOf(0.toUByte()), CameraStatus.PairingState.NEVER_PAIRED),
                Pair(ubyteArrayOf(1.toUByte()), CameraStatus.PairingState.STARTED),
                Pair(ubyteArrayOf(2.toUByte()), CameraStatus.PairingState.ABORTED),
                Pair(ubyteArrayOf(3.toUByte()), CameraStatus.PairingState.CANCELLED),
                Pair(ubyteArrayOf(4.toUByte()), CameraStatus.PairingState.COMPLETED)
            )
        }
    }

    @Test
    fun testStateMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.STATE.id.toUByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.STATE.name])
    }
}
