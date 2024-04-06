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
import com.bortxapps.simplebleclient.api.contracts.SimpleBleClient

internal class CommandsApi internal constructor(private val bleClient: SimpleBleClient) {

    fun subscribeToCameraSettingsChanges() =
        bleClient.subscription.subscribeToIncomeMessages()

    suspend fun getWifiApSSID() =
        bleClient.reader.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_SSID.uuid)

    suspend fun getWifiApPassword() =
        bleClient.reader.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_PASSWORD.uuid)

    suspend fun enableWifiAp() =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.EnableWifi.byteArray
        )

    suspend fun disableWifiAp() =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.DisableWifi.byteArray
        )

    suspend fun setPresetsVideo() =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsVideo.byteArray
        )

    suspend fun setShutterOff() =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetShutterOff.byteArray
        )

    suspend fun setShutterOn() =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetShutterOn.byteArray
        )

    suspend fun setPresetsPhoto() =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsPhoto.byteArray
        )

    suspend fun setPresetsTimeLapse() =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsTimeLapse.byteArray
        )

    suspend fun getOpenGoProVersion() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_COMMAND.uuid,
        GoProBleCommands.GetOpenGoProVersion.byteArray
    )

    //region query settings
    suspend fun getCameraStatus() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetCameraStatus.byteArray,

    )

    suspend fun getResolution() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetResolution.byteArray,

    )

    suspend fun getFrameRate() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetFrameRate.byteArray,

    )

    suspend fun getAutoPowerDown() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAutoPowerDown.byteArray,

    )

    suspend fun getAspectRatio() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAspectRatio.byteArray,

    )

    suspend fun getVideoLapseDigitalLenses() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoLapseDigitalLenses.byteArray,

    )

    suspend fun getPhotoLapseDigitalLenses() = bleClient.writer.sendDataWithResponse(

        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoLapseDigitalLenses.byteArray,

    )

    suspend fun getTimeLapseDigitalLenses() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetTimeLapseDigitalLenses.byteArray,

    )

    suspend fun getMediaFormat() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetMediaFormat.byteArray,

    )

    suspend fun getAntiFlicker() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetAntiFlicker.byteArray,

    )

    suspend fun getHyperSmooth() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHyperSmooth.byteArray,

    )

    suspend fun getPresets() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetActivePresetGroup.byteArray,
    )

    suspend fun getHorizonLeveling() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHorizonLeveling.byteArray,

    )

    suspend fun getMaxLens() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetMaxLens.byteArray,

    )

    suspend fun getHindsight() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetHindsight.byteArray,

    )

    suspend fun getInterval() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetInterval.byteArray,

    )

    suspend fun getDuration() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetDuration.byteArray,

    )

    suspend fun getVideoPerformanceMode() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoPerformanceMode.byteArray,

    )

    suspend fun getControls() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetControls.byteArray,

    )

    suspend fun getSpeed() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetSpeed.byteArray,

    )

    suspend fun getNightPhoto() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetNightPhoto.byteArray,

    )

    suspend fun getWirelessBand() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetWirelessBand.byteArray,

    )

    suspend fun getTrailLength() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetTrailLength.byteArray,

    )

    suspend fun getVideoMode() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoMode.byteArray,

    )

    suspend fun getVideoModeBitRate() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeBitRate.byteArray,

    )

    suspend fun getVideoModeBitDepth() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeBitDepth.byteArray,

    )

    suspend fun getVideoModeProfiles() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeProfiles.byteArray,

    )

    suspend fun getVideoModeAspectRatio() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeAspectRatio.byteArray,

    )

    suspend fun getVideoModeGoPro12() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetVideoModeGoPro12.byteArray,

    )

    suspend fun getLapseMode() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseMode.byteArray,

    )

    suspend fun getLapseModeAspectRatio() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeAspectRatio.byteArray,

    )

    suspend fun getLapseModeMaxLensMod() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeMaxLensMod.byteArray,

    )

    suspend fun getLapseModeMaxLensModEnabled() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetLapseModeMaxLensModEnabled.byteArray,

    )

    suspend fun getPhotoMode() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoMode.byteArray,

    )

    suspend fun getPhotoModeAspectRatio() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetPhotoModeAspectRatio.byteArray,

    )

    suspend fun getFraming() = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetFraming.byteArray,

    )
    //endregion

    suspend fun setResolution(resolution: Resolution) =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapResolutionToMessage(resolution)
        )

    suspend fun setFrameRate(frameRate: FrameRate) =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapFrameRateToMessage(frameRate)
        )

    suspend fun setHyperSmooth(hyperSmooth: HyperSmooth) =
        bleClient.writer.sendDataWithResponse(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            mapHyperSmoothToMessage(hyperSmooth)
        )

    suspend fun setSpeed(speed: Speed) = bleClient.writer.sendDataWithResponse(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_SETTING.uuid,
        mapSpeedToMessage(speed)
    )
}