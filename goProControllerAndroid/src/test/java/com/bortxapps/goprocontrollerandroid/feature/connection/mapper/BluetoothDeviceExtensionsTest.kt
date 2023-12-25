package com.bortxapps.goprocontrollerandroid.feature.connection.mapper

import android.bluetooth.BluetoothDevice
import com.bortxapps.goprocontrollerandroid.domain.data.PairedState
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

class BluetoothDeviceExtensionsTest {

    @Test
    fun `toMapCamera with known device name and bonded state`() {
        val device = mockk<BluetoothDevice>()
        every { device.name } returns "GoPro"
        every { device.address } returns "00:11:22:AA:BB:CC"
        every { device.bondState } returns BluetoothDevice.BOND_BONDED

        val mappedCamera = device.toMapCamera()

        assertEquals("GoPro", mappedCamera.name)
        assertEquals("00:11:22:AA:BB:CC", mappedCamera.address)
        assertEquals(PairedState.PAIRED_OTHER, mappedCamera.pairedState)
    }

    @Test
    fun `toMapCamera with unknown device name and unbonded state`() {
        val device = mockk<BluetoothDevice>()
        every { device.name } returns null
        every { device.address } returns "00:11:22:AA:BB:DD"
        every { device.bondState } returns BluetoothDevice.BOND_NONE

        val mappedCamera = device.toMapCamera()

        assertEquals("Unknown", mappedCamera.name)
        assertEquals("00:11:22:AA:BB:DD", mappedCamera.address)
        assertEquals(PairedState.UNPAIRED, mappedCamera.pairedState)
    }

    @Test
    fun `bondingStateToPairedState with different bonding states`() {
        assertEquals(PairedState.PAIRED_OTHER, bondingStateToPairedState(BluetoothDevice.BOND_BONDED))
        assertEquals(PairedState.PAIRING, bondingStateToPairedState(BluetoothDevice.BOND_BONDING))
        assertEquals(PairedState.UNPAIRED, bondingStateToPairedState(BluetoothDevice.BOND_NONE))
    }
}
