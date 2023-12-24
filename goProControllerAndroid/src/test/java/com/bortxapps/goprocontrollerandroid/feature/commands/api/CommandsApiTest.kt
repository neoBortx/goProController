package com.bortxapps.goprocontrollerandroid.feature.commands.api

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class CommandsApiTest {

    private lateinit var bleManager: BleManager
    private lateinit var commandsApi: CommandsApi

    @Before
    fun setUp() {
        bleManager = mockk(relaxed = true)
        commandsApi = CommandsApi(bleManager)
    }

    @Test
    fun `test getWifiApSSID calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.WIFI_AP_SERVICE.uuid
        val expectedUUIDChar = GoProUUID.WIFI_AP_SSID.uuid

        commandsApi.getWifiApSSID()

        coVerify { bleManager.readData(expectedUUIDService, expectedUUIDChar) }
    }

    @Test
    fun `test getWifiApPassword calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.WIFI_AP_SERVICE.uuid
        val expectedUUIDChar = GoProUUID.WIFI_AP_PASSWORD.uuid

        commandsApi.getWifiApPassword()

        coVerify { bleManager.readData(expectedUUIDService, expectedUUIDChar) }
    }

    @Test
    fun `test enableWifiAp calls sendData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.EnableWifi.byteArray

        commandsApi.enableWifiAp()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test disableWifiAp calls sendData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.DisableWifi.byteArray

        commandsApi.disableWifiAp()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setPresetsVideo calls sendData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.SetPresetsVideo.byteArray

        commandsApi.setPresetsVideo()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setShutterOff calls sendData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.SetShutterOff.byteArray

        commandsApi.setShutterOff()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setShutterOn calls sendData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.SetShutterOn.byteArray

        commandsApi.setShutterOn()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setPresetsPhoto calls sendData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.SetPresetsPhoto.byteArray

        commandsApi.setPresetsPhoto()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setPresetsTimeLapse calls sendData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.SetPresetsTimeLapse.byteArray

        commandsApi.setPresetsTimeLapse()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test getOpenGoProVersion calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_COMMAND.uuid
        val expectedData = GoProBleCommands.GetOpenGoProVersion.byteArray

        commandsApi.getOpenGoProVersion()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test getCameraStatus calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_QUERY.uuid
        val expectedData = GoProBleCommands.GetCameraStatus.byteArray

        commandsApi.getCameraStatus()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData, true) }
    }

    @Test
    fun `test getResolution calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_QUERY.uuid
        val expectedData = GoProBleCommands.GetResolution.byteArray

        commandsApi.getResolution()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData, true) }
    }

    @Test
    fun `test getFrameRate calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_QUERY.uuid
        val expectedData = GoProBleCommands.GetFrameRate.byteArray

        commandsApi.getFrameRate()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData, true) }
    }

    @Test
    fun `test getHyperSmooth calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_QUERY.uuid
        val expectedData = GoProBleCommands.GetHyperSmooth.byteArray

        commandsApi.getHyperSmooth()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData, true) }
    }

    @Test
    fun `test getPresets calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_QUERY.uuid
        val expectedData = GoProBleCommands.GetActivePresetGroup.byteArray

        commandsApi.getPresets()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData, true) }
    }

    @Test
    fun `test getSpeed calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_QUERY.uuid
        val expectedData = GoProBleCommands.GetSpeed.byteArray

        commandsApi.getSpeed()

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData, true) }
    }

    @Test
    fun `test setResolution calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_SETTING.uuid
        val expectedData = GoProBleCommands.SetResolution2K.byteArray

        commandsApi.setResolution(Resolution.RESOLUTION_2_7K)

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setFrameRate calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_SETTING.uuid
        val expectedData = GoProBleCommands.SetFrameRate120.byteArray

        commandsApi.setFrameRate(FrameRate.FRAME_RATE_120)

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setHyperSmooth calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_SETTING.uuid
        val expectedData = GoProBleCommands.SetHyperSmoothHigh.byteArray

        commandsApi.setHyperSmooth(HyperSmooth.HIGH)

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }

    @Test
    fun `test setSpeed calls readData with correct parameters`() = runTest {
        val expectedUUIDService = GoProUUID.SERVICE_UUID.uuid
        val expectedUUIDChar = GoProUUID.CQ_SETTING.uuid
        val expectedData = GoProBleCommands.SetSpeedUltraSlowMo.byteArray

        commandsApi.setSpeed(Speed.ULTRA_SLOW_MO_8X)

        coVerify { bleManager.sendData(expectedUUIDService, expectedUUIDChar, expectedData) }
    }
}
