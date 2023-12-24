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
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager

internal class CommandsApi internal constructor(private val bleManager: BleManager) {

    suspend fun getWifiApSSID() =
        bleManager.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_SSID.uuid)

    suspend fun getWifiApPassword() =
        bleManager.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_PASSWORD.uuid)

    suspend fun enableWifiAp() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.EnableWifi.byteArray
        )

    suspend fun disableWifiAp() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.DisableWifi.byteArray
        )

    suspend fun setPresetsVideo() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsVideo.byteArray
        )

    suspend fun setShutterOff() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetShutterOff.byteArray
        )

    suspend fun setShutterOn() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetShutterOn.byteArray
        )

    suspend fun setPresetsPhoto() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsPhoto.byteArray
        )

    suspend fun setPresetsTimeLapse() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsTimeLapse.byteArray
        )

    suspend fun getOpenGoProVersion() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_COMMAND.uuid,
        GoProBleCommands.GetOpenGoProVersion.byteArray
    )

    //region query settings
    suspend fun getCameraStatus() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetCameraStatus.byteArray,
        complexResponse = true
    )

    suspend fun getResolution() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetResolution.byteArray,
        complexResponse = true
    )

    suspend fun getFrameRate() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetFrameRate.byteArray,
        complexResponse = true
    )

    suspend fun getAutoPowerDown() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAutoPowerDown.byteArray,
        complexResponse = true
    )

    suspend fun getAspectRatio() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getVideoLapseDigitalLenses() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoLapseDigitalLenses.byteArray,
        complexResponse = true
    )

    suspend fun getPhotoLapseDigitalLenses() = bleManager.sendData(

        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoLapseDigitalLenses.byteArray,
        complexResponse = true
    )

    suspend fun getTimeLapseDigitalLenses() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetTimeLapseDigitalLenses.byteArray,
        complexResponse = true
    )

    suspend fun getMediaFormat() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetMediaFormat.byteArray,
        complexResponse = true
    )

    suspend fun getAntiFlicker() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAntiFlicker.byteArray,
        complexResponse = true
    )

    suspend fun getHyperSmooth() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHyperSmooth.byteArray,
        complexResponse = true
    )

    suspend fun getPresets() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetActivePresetGroup.byteArray,
        complexResponse = true
    )

    suspend fun getHorizonLeveling() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHorizonLeveling.byteArray,
        complexResponse = true
    )

    suspend fun getMaxLens() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetMaxLens.byteArray,
        complexResponse = true
    )

    suspend fun getHindsight() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHindsight.byteArray,
        complexResponse = true
    )

    suspend fun getInterval() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetInterval.byteArray,
        complexResponse = true
    )

    suspend fun getDuration() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetDuration.byteArray,
        complexResponse = true
    )

    suspend fun GetVideoPerformanceMode() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoPerformanceMode.byteArray,
        complexResponse = true
    )

    suspend fun getControls() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetControls.byteArray,
        complexResponse = true
    )

    suspend fun getSpeed() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetSpeed.byteArray,
        complexResponse = true
    )

    suspend fun getNightPhoto() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetNightPhoto.byteArray,
        complexResponse = true
    )

    suspend fun getWirelessBand() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetWirelessBand.byteArray,
        complexResponse = true
    )

    suspend fun getTrailLength() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetTrailLength.byteArray,
        complexResponse = true
    )

    suspend fun getVideoMode() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoMode.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeBitRate() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeBitRate.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeBitDepth() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeBitDepth.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeProfiles() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeProfiles.byteArray,
        complexResponse = true
    )

    suspend fun getVideoModeAspectRatio() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getVideoMode_GoPro_12() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeGoPro12.byteArray,
        complexResponse = true
    )

    suspend fun getLapseMode() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseMode.byteArray,
        complexResponse = true
    )

    suspend fun getLapseModeAspectRatio() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getLapseModeMaxLensMod() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeMaxLensMod.byteArray,
        complexResponse = true
    )

    suspend fun getLapseModeMaxLensModEnabled() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeMaxLensModEnabled.byteArray,
        complexResponse = true
    )

    suspend fun getPhotoMode() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoMode.byteArray,
        complexResponse = true
    )

    suspend fun getPhotoModeAspectRatio() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoModeAspectRatio.byteArray,
        complexResponse = true
    )

    suspend fun getFraming() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetFraming.byteArray,
        complexResponse = true
    )
    //endregion

    suspend fun setResolution(resolution: Resolution) =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapResolutionToMessage(resolution)
        )

    suspend fun setFrameRate(frameRate: FrameRate) =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapFrameRateToMessage(frameRate)
        )

    suspend fun setHyperSmooth(hyperSmooth: HyperSmooth) =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapHyperSmoothToMessage(hyperSmooth)
        )

    suspend fun setSpeed(speed: Speed) = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_SETTING.uuid,
        mapSpeedToMessage(speed)
    )
}