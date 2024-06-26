package com.bortxapps.goprocontrollerandroid.feature.commands

import android.content.Context
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.feature.commands.api.CommandsApi
import com.bortxapps.goprocontrollerandroid.feature.commands.data.CameraStatus
import com.bortxapps.goprocontrollerandroid.feature.commands.decoder.decodeMessageAsMap
import com.bortxapps.simplebleclient.api.data.BleNetworkMessage

import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.UUID

class GoProCommandsImplTest {

    private lateinit var goProCommandsImpl: GoProCommandsImpl
    private val mockedContext = mockk<Context>(relaxed = true)
    private val mockedApi = mockk<CommandsApi>(relaxed = true)
    private val decodedResponseString = "response from API"
    private val decodedBytesSuccess = byteArrayOf(0x01, 0x00)
    private val decodedBytesError = byteArrayOf(0x51, 0xAB.toByte())
    private val characteristicId = UUID.randomUUID()

    private val responseString = BleNetworkMessage(characteristicId,  decodedResponseString.toByteArray(Charsets.UTF_8))
    private val responseBytesSuccess = BleNetworkMessage(characteristicId,  decodedBytesSuccess)
    private val responseBytesError = BleNetworkMessage(characteristicId, decodedBytesError)

    @Before
    fun setUp() {

        mockkStatic(CameraStatus::class)
        mockkStatic(::decodeMessageAsMap)
        goProCommandsImpl = GoProCommandsImpl(mockedContext, mockedApi)
    }

    @Test
    fun testGetWifiApSSID() = runTest {
        coEvery { mockedApi.getWifiApSSID() } returns responseString
        assertEquals(decodedResponseString, goProCommandsImpl.getWifiApSSID().getOrNull())
    }

    @Test
    fun testGetWifiApPassword() = runTest {
        coEvery { mockedApi.getWifiApPassword() } returns responseString
        assertEquals(decodedResponseString, goProCommandsImpl.getWifiApPassword().getOrNull())
    }

    //region enableWifiAp
    @Test
    fun testEnableWifiAp_success() = runTest {
        coEvery { mockedApi.enableWifiAp() } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.enableWifiAp().getOrNull())
    }

    @Test
    fun testEnableWifiAp_error() = runTest {
        coEvery { mockedApi.enableWifiAp() } returns responseBytesError
        assertEquals(GoProError.SEND_COMMAND_REJECTED, (goProCommandsImpl.enableWifiAp().exceptionOrNull() as GoProException).goProError)
    }
    //endregion

    //region disableWifiAp
    @Test
    fun testDisableWifiAp_success() = runTest {
        coEvery { mockedApi.disableWifiAp() } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.disableWifiAp().getOrNull())
    }

    @Test
    fun testDisableWifiAp_error() = runTest {
        coEvery { mockedApi.disableWifiAp() } returns responseBytesError
        assertEquals(GoProError.SEND_COMMAND_REJECTED, (goProCommandsImpl.disableWifiAp().exceptionOrNull() as GoProException).goProError)
    }
    //endregion

    //region setPresetsVideo
    @Test
    fun testSetPresetsVideo_success() = runTest {
        coEvery { mockedApi.setPresetsVideo() } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setPresetsVideo().getOrNull())
    }

    @Test
    fun testSetPresetsVideo_error() = runTest {
        coEvery { mockedApi.setPresetsVideo() } returns responseBytesError
        assertEquals(GoProError.SEND_COMMAND_REJECTED, (goProCommandsImpl.setPresetsVideo().exceptionOrNull() as GoProException).goProError)
    }
    //endregion

    //region setPresetsPhoto
    @Test
    fun testSetPresetsPhoto_success() = runTest {
        coEvery { mockedApi.setPresetsPhoto() } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setPresetsPhoto().getOrNull())
    }

    @Test
    fun testSetPresetsPhoto_error() = runTest {
        coEvery { mockedApi.setPresetsPhoto() } returns responseBytesError
        assertEquals(GoProError.SEND_COMMAND_REJECTED, (goProCommandsImpl.setPresetsPhoto().exceptionOrNull() as GoProException).goProError)
    }
    //endregion

    //region setPresetsTimeLapse
    @Test
    fun testSetPresetsTimeLapse_success() = runTest {
        coEvery { mockedApi.setPresetsTimeLapse() } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setPresetsTimeLapse().getOrNull())
    }

    @Test
    fun testSetPresetsTimeLapse_error() = runTest {
        coEvery { mockedApi.setPresetsTimeLapse() } returns responseBytesError
        assertEquals(GoProError.SEND_COMMAND_REJECTED, (goProCommandsImpl.setPresetsTimeLapse().exceptionOrNull() as GoProException).goProError)
    }
    //endregion

    //region setShutterOff
    @Test
    fun testSetShutterOff_success() = runTest {
        coEvery { mockedApi.setShutterOff() } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setShutterOff().getOrNull())
    }

    @Test
    fun testSetShutterOff_error() = runTest {
        coEvery { mockedApi.setShutterOff() } returns responseBytesError
        assertEquals(GoProError.SEND_COMMAND_REJECTED, (goProCommandsImpl.setShutterOff().exceptionOrNull() as GoProException).goProError)
    }
    //endregion

    //region setShutterOn
    @Test
    fun testSetShutterOn_success() = runTest {
        coEvery { mockedApi.setShutterOn() } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setShutterOn().getOrNull())
    }

    @Test
    fun testSetShutterOn_error() = runTest {
        coEvery { mockedApi.setShutterOn() } returns responseBytesError
        assertEquals(GoProError.SEND_COMMAND_REJECTED, (goProCommandsImpl.setShutterOn().exceptionOrNull() as GoProException).goProError)
    }
    //endregion

    //region setResolution
    @Test
    fun testSetResolution_success() = runTest {
        coEvery { mockedApi.setResolution(Resolution.RESOLUTION_2_7K) } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setResolution(Resolution.RESOLUTION_2_7K).getOrNull())
    }

    @Test
    fun testSetResolution_error() = runTest {
        coEvery { mockedApi.setResolution(Resolution.RESOLUTION_2_7K) } returns responseBytesError
        assertEquals(
            GoProError.SEND_COMMAND_REJECTED,
            (goProCommandsImpl.setResolution(Resolution.RESOLUTION_2_7K).exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region setFramerate
    @Test
    fun testSetFramerate_success() = runTest {
        coEvery { mockedApi.setFrameRate(FrameRate.FRAME_RATE_120) } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setFrameRate(FrameRate.FRAME_RATE_120).getOrNull())
    }

    @Test
    fun testSetFramerate_error() = runTest {
        coEvery { mockedApi.setFrameRate(FrameRate.FRAME_RATE_120) } returns responseBytesError
        assertEquals(
            GoProError.SEND_COMMAND_REJECTED,
            (goProCommandsImpl.setFrameRate(FrameRate.FRAME_RATE_120).exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region setHyperSmooth
    @Test
    fun testSetHyperSmooth_success() = runTest {
        coEvery { mockedApi.setHyperSmooth(HyperSmooth.BOOST) } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setHyperSmooth(HyperSmooth.BOOST).getOrNull())
    }

    @Test
    fun testSetHyperSmooth_error() = runTest {
        coEvery { mockedApi.setHyperSmooth(HyperSmooth.BOOST) } returns responseBytesError
        assertEquals(
            GoProError.SEND_COMMAND_REJECTED,
            (goProCommandsImpl.setHyperSmooth(HyperSmooth.BOOST).exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region setSpeed
    @Test
    fun testSetSpeed_success() = runTest {
        coEvery { mockedApi.setSpeed(Speed.SUPER_SLOW_MO_4X_17K) } returns responseBytesSuccess
        assertEquals(true, goProCommandsImpl.setSpeed(Speed.SUPER_SLOW_MO_4X_17K).getOrNull())
    }

    @Test
    fun testSetSpeed_error() = runTest {
        coEvery { mockedApi.setSpeed(Speed.SUPER_SLOW_MO_4X_17K) } returns responseBytesError
        assertEquals(
            GoProError.SEND_COMMAND_REJECTED,
            (goProCommandsImpl.setSpeed(Speed.SUPER_SLOW_MO_4X_17K).exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region getOpenGoProVersion
    @Test
    fun testGetOpenGoProVersion_success() = runTest {
        val responseVersion = BleNetworkMessage(characteristicId, byteArrayOf(0x09, 0x02))
        coEvery { mockedApi.getOpenGoProVersion() } returns responseVersion
        assertEquals("9.2", goProCommandsImpl.getOpenGoProVersion().getOrNull())
    }
    //endregion

    //region getResolution
    @Test
    fun testGetResolution_success() = runTest {
        val responseResolution = BleNetworkMessage(characteristicId, byteArrayOf(3, 2, 1, 100))
        coEvery { mockedApi.getResolution() } returns responseResolution
        assertEquals(Resolution.RESOLUTION_5_3K, goProCommandsImpl.getResolution().getOrNull())
    }

    @Test
    fun testGetResolution_error() = runTest {
        coEvery { mockedApi.getResolution() } returns responseBytesError
        assertEquals(
            GoProError.UNABLE_TO_MAP_DATA,
            (goProCommandsImpl.getResolution().exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region getFrameRate
    @Test
    fun testGetFrameRate_success() = runTest {
        val responseFrameRate = BleNetworkMessage(characteristicId, byteArrayOf(3, 3, 1, 0))
        coEvery { mockedApi.getFrameRate() } returns responseFrameRate
        assertEquals(FrameRate.FRAME_RATE_240, goProCommandsImpl.getFrameRate().getOrNull())
    }

    @Test
    fun testGetFrameRate_error() = runTest {
        coEvery { mockedApi.getFrameRate() } returns responseBytesError
        assertEquals(
            GoProError.UNABLE_TO_MAP_DATA,
            (goProCommandsImpl.getFrameRate().exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region getHyperSmooth
    @Test
    fun testGetHyperSmooth_success() = runTest {
        val responseHyperSmooth = BleNetworkMessage(characteristicId, byteArrayOf(3, 0x87.toByte(), 1, 0))
        coEvery { mockedApi.getHyperSmooth() } returns responseHyperSmooth
        assertEquals(HyperSmooth.OFF, goProCommandsImpl.getHyperSmooth().getOrNull())
    }

    @Test
    fun testGetHyperSmooth_error() = runTest {
        coEvery { mockedApi.getHyperSmooth() } returns responseBytesError
        assertEquals(
            GoProError.UNABLE_TO_MAP_DATA,
            (goProCommandsImpl.getHyperSmooth().exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region getSpeed
    @Test
    fun testGetSpeed_success() = runTest {
        val responseSpeed = BleNetworkMessage(characteristicId, byteArrayOf(3, 0xB0.toByte(), 1, 3))
        coEvery { mockedApi.getSpeed() } returns responseSpeed
        assertEquals(Speed.REGULAR_1X, goProCommandsImpl.getSpeed().getOrNull())
    }

    @Test
    fun testGetSpeed_error() = runTest {
        coEvery { mockedApi.getSpeed() } returns responseBytesError

        assertEquals(
            GoProError.UNABLE_TO_MAP_DATA,
            (goProCommandsImpl.getSpeed().exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region getPresets
    @Test
    fun testGetPresets_success() = runTest {
        val responsePresets = BleNetworkMessage(characteristicId, byteArrayOf(4, 62, 2, 3, 0xE8.toByte()))
        coEvery { mockedApi.getPresets() } returns responsePresets
        assertEquals(Presets.VIDEO, goProCommandsImpl.getPresets().getOrNull())
    }

    @Test
    fun testGetPresets_error() = runTest {
        coEvery { mockedApi.getPresets() } returns responseBytesError
        assertEquals(
            GoProError.UNABLE_TO_MAP_DATA,
            (goProCommandsImpl.getPresets().exceptionOrNull() as GoProException).goProError
        )
    }
    //endregion

    //region getCameraStatus
    @Test
    fun testGetCameraStatus_success() = runTest {
        val ubyte: Byte = 0x01
        val responseCameraStatus = BleNetworkMessage(characteristicId, byteArrayOf(4))
        val map = mapOf(ubyte to byteArrayOf(0x01))
        val result = mapOf("INTERNAL_BATTERY_PRESENT" to "true")

        coEvery { mockedApi.getCameraStatus() } returns responseCameraStatus
        coEvery { decodeMessageAsMap(responseCameraStatus) } returns map

        assertEquals(result, goProCommandsImpl.getCameraStatus().getOrNull())
    }



}