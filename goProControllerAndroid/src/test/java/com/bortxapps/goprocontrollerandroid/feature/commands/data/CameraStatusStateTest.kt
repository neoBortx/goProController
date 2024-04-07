package com.bortxapps.goprocontrollerandroid.feature.commands.data

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@OptIn(ExperimentalUnsignedTypes::class)
@RunWith(Parameterized::class)
class CameraStatusStateTest(
    private val data: Pair<ByteArray, CameraStatus.PairingState>
) {

    companion object {
        @JvmStatic
        @Parameterized.Parameters(name = "{index}: Test with byteArrayValue={0}, expectedEnum={1}")
        fun data(): Collection<Pair<ByteArray, CameraStatus.PairingState>> {
            return listOf(
                Pair(byteArrayOf(0), CameraStatus.PairingState.NEVER_PAIRED),
                Pair(byteArrayOf(1), CameraStatus.PairingState.STARTED),
                Pair(byteArrayOf(2), CameraStatus.PairingState.ABORTED),
                Pair(byteArrayOf(3), CameraStatus.PairingState.CANCELLED),
                Pair(byteArrayOf(4), CameraStatus.PairingState.COMPLETED)
            )
        }
    }

    @Test
    fun testStateMapping() {
        val bytes = data.first
        val expected = data.second

        val mockData = mapOf(
            CameraStatus.CameraStatusIds.STATE.id.toByte() to bytes
        )
        val result = CameraStatus.decodeStatus(mockData)
        assertEquals(expected.toString(), result[CameraStatus.CameraStatusIds.STATE.name])
    }
}
