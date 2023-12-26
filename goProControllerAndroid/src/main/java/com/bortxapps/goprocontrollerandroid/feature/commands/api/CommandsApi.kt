package com.bortxapps.goprocontrollerandroid.feature.commands.api

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapFrameRateToMessage
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapHyperSmoothToMessage
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapResolutionToMessage
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapSpeedToMessage
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.simplebleclient.manager.contracts.SimpleBleClient

internal class CommandsApi internal constructor(private val bleClient: SimpleBleClient) {

    suspend fun getWifiApSSID() =
        bleClient.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_SSID.uuid)

    suspend fun getWifiApPassword() =
        bleClient.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_PASSWORD.uuid)

    suspend fun enableWifiAp() =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.EnableWifi.byteArray
        )

    suspend fun disableWifiAp() =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.DisableWifi.byteArray
        )

    suspend fun setPresetsVideo() =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsVideo.byteArray
        )

    suspend fun setShutterOff() =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetShutterOff.byteArray
        )

    suspend fun setShutterOn() =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetShutterOn.byteArray
        )

    suspend fun setPresetsPhoto() =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsPhoto.byteArray
        )

    suspend fun setPresetsTimeLapse() =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsTimeLapse.byteArray
        )

    suspend fun getOpenGoProVersion() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_COMMAND.uuid,
        GoProBleCommands.GetOpenGoProVersion.byteArray
    )

    //region query settings
    suspend fun getCameraStatus() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetCameraStatus.byteArray,
        complexResponse = true
    )

    suspend fun getResolution() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetResolution.byteArray,
        complexResponse = true
    )

    suspend fun getFrameRate() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetFrameRate.byteArray,
        complexResponse = true
    )

    suspend fun getAutoPowerDown() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAutoPowerDown.byteArray,
        complexResponse = true
    )

    suspend fun getAspectRatio() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getVideoLapseDigitalLenses() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoLapseDigitalLenses.byteArray,
        complexResponse = true
    )

    suspend fun getPhotoLapseDigitalLenses() = bleClient.sendData(

        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoLapseDigitalLenses.byteArray,
        complexResponse = true
    )

    suspend fun getTimeLapseDigitalLenses() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetTimeLapseDigitalLenses.byteArray,
        complexResponse = true
    )

    suspend fun getMediaFormat() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetMediaFormat.byteArray,
        complexResponse = true
    )

    suspend fun getAntiFlicker() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAntiFlicker.byteArray,
        complexResponse = true
    )

    suspend fun getHyperSmooth() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHyperSmooth.byteArray,
        complexResponse = true
    )

    suspend fun getPresets() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetActivePresetGroup.byteArray,
        complexResponse = true
    )

    suspend fun getHorizonLeveling() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHorizonLeveling.byteArray,
        complexResponse = true
    )

    suspend fun getMaxLens() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetMaxLens.byteArray,
        complexResponse = true
    )

    suspend fun getHindsight() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHindsight.byteArray,
        complexResponse = true
    )

    suspend fun getInterval() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetInterval.byteArray,
        complexResponse = true
    )

    suspend fun getDuration() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetDuration.byteArray,
        complexResponse = true
    )

    suspend fun getVideoPerformanceMode() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoPerformanceMode.byteArray,
        complexResponse = true
    )

    suspend fun getControls() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetControls.byteArray,
        complexResponse = true
    )

    suspend fun getSpeed() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetSpeed.byteArray,
        complexResponse = true
    )

    suspend fun getNightPhoto() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetNightPhoto.byteArray,
        complexResponse = true
    )

    suspend fun getWirelessBand() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetWirelessBand.byteArray,
        complexResponse = true
    )

    suspend fun getTrailLength() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetTrailLength.byteArray,
        complexResponse = true
    )

    suspend fun getVideoMode() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoMode.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeBitRate() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeBitRate.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeBitDepth() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeBitDepth.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeProfiles() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeProfiles.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeAspectRatio() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeGoPro12() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeGoPro12.byteArray,
        complexResponse = true
    )

    suspend fun getLapseMode() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseMode.byteArray,
        complexResponse = true
    )

    suspend fun getLapseModeAspectRatio() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getLapseModeMaxLensMod() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeMaxLensMod.byteArray,
        complexResponse = true
    )

    suspend fun getLapseModeMaxLensModEnabled() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeMaxLensModEnabled.byteArray,
        complexResponse = true
    )

    suspend fun getPhotoMode() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoMode.byteArray,
        complexResponse = true
    )

    suspend fun getPhotoModeAspectRatio() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoModeAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getFraming() = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetFraming.byteArray,
        complexResponse = true
    )
    //endregion

    suspend fun setResolution(resolution: Resolution) =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapResolutionToMessage(resolution)
        )

    suspend fun setFrameRate(frameRate: FrameRate) =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapFrameRateToMessage(frameRate)
        )

    suspend fun setHyperSmooth(hyperSmooth: HyperSmooth) =
        bleClient.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapHyperSmoothToMessage(hyperSmooth)
        )

    suspend fun setSpeed(speed: Speed) = bleClient.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_SETTING.uuid,
        mapSpeedToMessage(speed)
    )
}