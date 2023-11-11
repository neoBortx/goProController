package com.bortxapps.goprocontrollerandroid.feature.commands.api

import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager

class CommandsApi(private val bleManager: BleManager = BleManager.instance) {

    suspend fun getWifiApSSID() =
        bleManager.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_SSID.uuid)

    suspend fun getWifiApPassword() =
        bleManager.readData(GoProUUID.WIFI_AP_SERVICE.uuid, GoProUUID.WIFI_AP_PASSWORD.uuid)

    suspend fun enableWifiAp() =
        bleManager.sendData(
            GoProUUID.WIFI_AP_SERVICE.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.EnableWifi.byteArray
        )

    suspend fun disableWifiAp() =
        bleManager.sendData(
            GoProUUID.WIFI_AP_SERVICE.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.DisableWifi.byteArray
        )

    suspend fun setPresetsVideo() =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_COMMAND.uuid,
            GoProBleCommands.SetPresetsVideo.byteArray
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

    suspend fun getHardwareInfo() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_COMMAND.uuid,
        GoProBleCommands.GetHardwareInfo.byteArray,
        complexResponse = true
    )

    suspend fun getCameraSettings() = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_QUERY.uuid,
        GoProBleCommands.GetCameraSettings.byteArray,
        complexResponse = true
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
            when (resolution) {
                Resolution.RESOLUTION_5_3K -> GoProBleCommands.SetResolution53K.byteArray
                Resolution.RESOLUTION_4K -> GoProBleCommands.SetResolution4K.byteArray
                Resolution.RESOLUTION_4K_4_3 -> GoProBleCommands.SetResolution4K43.byteArray
                Resolution.RESOLUTION_2_7K -> GoProBleCommands.SetResolution2K.byteArray
                Resolution.RESOLUTION_2_7K_4_3 -> GoProBleCommands.SetResolution2K43.byteArray
                Resolution.RESOLUTION_1440 -> GoProBleCommands.SetResolution1440.byteArray
                Resolution.RESOLUTION_1080 -> GoProBleCommands.SetResolution1080.byteArray
                Resolution.RESOLUTION_4K_GOPRO_12 -> GoProBleCommands.SetResolution4KGoPro12.byteArray
                Resolution.RESOLUTION_4K_4_3_GOPRO_12 -> GoProBleCommands.SetResolution4K43GoPro12.byteArray
                Resolution.RESOLUTION_5K_GOPRO_12 -> GoProBleCommands.SetResolution5KGoPro12.byteArray
                Resolution.RESOLUTION_1080_GOPRO_12 -> GoProBleCommands.SetResolution1080GoPro12.byteArray
                Resolution.RESOLUTION_2K_GO_PRO_12 -> GoProBleCommands.SetResolution2KGoPro12.byteArray
                Resolution.RESOLUTION_2K_4_3_GO_PRO_12 -> GoProBleCommands.SetResolution2K43GoPro12.byteArray
            }
        )

    suspend fun setFrameRate(frameRate: FrameRate) =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            when (frameRate) {
                FrameRate.FRAME_RATE_240 -> GoProBleCommands.SetFrameRate240.byteArray
                FrameRate.FRAME_RATE_200 -> GoProBleCommands.SetFrameRate200.byteArray
                FrameRate.FRAME_RATE_120 -> GoProBleCommands.SetFrameRate120.byteArray
                FrameRate.FRAME_RATE_100 -> GoProBleCommands.SetFrameRate100.byteArray
                FrameRate.FRAME_RATE_60 -> GoProBleCommands.SetFrameRate60.byteArray
                FrameRate.FRAME_RATE_50 -> GoProBleCommands.SetFrameRate50.byteArray
                FrameRate.FRAME_RATE_30 -> GoProBleCommands.SetFrameRate30.byteArray
                FrameRate.FRAME_RATE_25 -> GoProBleCommands.SetFrameRate25.byteArray
                FrameRate.FRAME_RATE_24 -> GoProBleCommands.SetFrameRate24.byteArray
            }
        )

    suspend fun setHyperSmooth(hyperSmooth: HyperSmooth) =
        bleManager.sendData(
            GoProUUID.SERVICE_UUID.uuid,
            GoProUUID.CQ_SETTING.uuid,
            when (hyperSmooth) {
                HyperSmooth.OFF -> GoProBleCommands.SetHyperSmoothOff.byteArray
                HyperSmooth.LOW -> GoProBleCommands.SetHyperSmoothLow.byteArray
                HyperSmooth.HIGH -> GoProBleCommands.SetHyperSmoothHigh.byteArray
                HyperSmooth.BOOST -> GoProBleCommands.SetHyperSmoothBoost.byteArray
                HyperSmooth.AUTO -> GoProBleCommands.SetHyperSmoothAuto.byteArray
                HyperSmooth.STANDARD -> GoProBleCommands.SetHyperSmoothStandard.byteArray
            }
        )

    suspend fun setSpeed(speed: Speed) = bleManager.sendData(
        GoProUUID.SERVICE_UUID.uuid,
        GoProUUID.CQ_SETTING.uuid,
        when (speed) {
            Speed.REGULAR_1X -> GoProBleCommands.SetSpeedNormal.byteArray
            Speed.SLOW_MO_2X -> GoProBleCommands.SetSpeedSlow.byteArray
            Speed.SUPER_SLOW_MO_4X -> GoProBleCommands.SetSpeedSuperSLowMo.byteArray
            Speed.ULTRA_SLOW_MO_8X -> GoProBleCommands.SetSpeedUltraSlowMo.byteArray
            Speed.SLOW_MO_LONG_BATTERY_2X -> GoProBleCommands.SetSpeedSlowMoLongBattery.byteArray
            Speed.SUPER_SLOW_MO_LONG_BATTERY_4X -> GoProBleCommands.SetSpeedSuperSlowMoLongBattery.byteArray
            Speed.ULTRA_SLOW_MO_LONG_BATTERY_8X -> GoProBleCommands.SetSpeedUltraSlowMoLongBattery.byteArray
            Speed.SLOW_MO_2X_4K -> GoProBleCommands.SetSpeedSlow4K.byteArray
            Speed.SUPER_SLOW_MO_4X_17K -> GoProBleCommands.SetSpeedSuperSLowMo27k.byteArray
        }
    )
}