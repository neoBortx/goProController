package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.domain.data.GoProSettings
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.feature.commands.data.FRAME_RATE_CHARACTERISTIC_ID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import com.bortxapps.goprocontrollerandroid.feature.commands.data.HYPER_SMOOTH_CHARACTERISTIC_ID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.PRESETS_CHARACTERISTIC_ID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.RESOLUTION_CHARACTERISTIC_ID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.SHUTTER_CHARACTERISTIC_ID
import com.bortxapps.goprocontrollerandroid.feature.commands.data.SPEED_CHARACTERISTIC_ID

internal fun mapResolution(data: Byte): Resolution {
    return when {
        (data == (GoProBleCommands.SetResolution53K.byteArray.last())) -> Resolution.RESOLUTION_5_3K
        (data == (GoProBleCommands.SetResolution4K.byteArray.last())) -> Resolution.RESOLUTION_4K
        (data == (GoProBleCommands.SetResolution4K43.byteArray.last())) -> Resolution.RESOLUTION_4K_4_3
        (data == (GoProBleCommands.SetResolution2K.byteArray.last())) -> Resolution.RESOLUTION_2_7K
        (data == (GoProBleCommands.SetResolution2K43.byteArray.last())) -> Resolution.RESOLUTION_2_7K_4_3
        (data == (GoProBleCommands.SetResolution1440.byteArray.last())) -> Resolution.RESOLUTION_1440
        (data == (GoProBleCommands.SetResolution1080.byteArray.last())) -> Resolution.RESOLUTION_1080
        (data == (GoProBleCommands.SetResolution5KGoPro12.byteArray.last())) -> Resolution.RESOLUTION_5K_GOPRO_12
        (data == (GoProBleCommands.SetResolution4KGoPro12.byteArray.last())) -> Resolution.RESOLUTION_4K_GOPRO_12
        (data == (GoProBleCommands.SetResolution4K43GoPro12.byteArray.last())) -> Resolution.RESOLUTION_4K_4_3_GOPRO_12
        (data == (GoProBleCommands.SetResolution2KGoPro12.byteArray.last())) -> Resolution.RESOLUTION_2K_GO_PRO_12
        (data == (GoProBleCommands.SetResolution2K43GoPro12.byteArray.last())) -> Resolution.RESOLUTION_2K_4_3_GO_PRO_12
        (data == (GoProBleCommands.SetResolution1080GoPro12.byteArray.last())) -> Resolution.RESOLUTION_1080_GOPRO_12
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

internal fun mapResolutionToMessage(resolution: Resolution): ByteArray = when (resolution) {
    Resolution.RESOLUTION_5_3K -> GoProBleCommands.SetResolution53K.byteArray
    Resolution.RESOLUTION_4K -> GoProBleCommands.SetResolution4K.byteArray
    Resolution.RESOLUTION_4K_4_3 -> GoProBleCommands.SetResolution4K43.byteArray
    Resolution.RESOLUTION_2_7K -> GoProBleCommands.SetResolution2K.byteArray
    Resolution.RESOLUTION_2_7K_4_3 -> GoProBleCommands.SetResolution2K43.byteArray
    Resolution.RESOLUTION_1440 -> GoProBleCommands.SetResolution1440.byteArray
    Resolution.RESOLUTION_1080 -> GoProBleCommands.SetResolution1080.byteArray
    Resolution.RESOLUTION_5K_GOPRO_12 -> GoProBleCommands.SetResolution5KGoPro12.byteArray
    Resolution.RESOLUTION_4K_GOPRO_12 -> GoProBleCommands.SetResolution4KGoPro12.byteArray
    Resolution.RESOLUTION_4K_4_3_GOPRO_12 -> GoProBleCommands.SetResolution4K43GoPro12.byteArray
    Resolution.RESOLUTION_2K_GO_PRO_12 -> GoProBleCommands.SetResolution2KGoPro12.byteArray
    Resolution.RESOLUTION_2K_4_3_GO_PRO_12 -> GoProBleCommands.SetResolution2K43GoPro12.byteArray
    Resolution.RESOLUTION_1080_GOPRO_12 -> GoProBleCommands.SetResolution1080GoPro12.byteArray
}

internal fun mapFrameRate(data: Byte): FrameRate {
    return when {
        (data == (GoProBleCommands.SetFrameRate240.byteArray.last())) -> FrameRate.FRAME_RATE_240
        (data == (GoProBleCommands.SetFrameRate200.byteArray.last())) -> FrameRate.FRAME_RATE_200
        (data == (GoProBleCommands.SetFrameRate120.byteArray.last())) -> FrameRate.FRAME_RATE_120
        (data == (GoProBleCommands.SetFrameRate100.byteArray.last())) -> FrameRate.FRAME_RATE_100
        (data == (GoProBleCommands.SetFrameRate60.byteArray.last())) -> FrameRate.FRAME_RATE_60
        (data == (GoProBleCommands.SetFrameRate50.byteArray.last())) -> FrameRate.FRAME_RATE_50
        (data == (GoProBleCommands.SetFrameRate30.byteArray.last())) -> FrameRate.FRAME_RATE_30
        (data == (GoProBleCommands.SetFrameRate25.byteArray.last())) -> FrameRate.FRAME_RATE_25
        (data == (GoProBleCommands.SetFrameRate24.byteArray.last())) -> FrameRate.FRAME_RATE_24
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

internal fun mapFrameRateToMessage(frameRate: FrameRate): ByteArray = when (frameRate) {
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

internal fun mapHyperSmooth(data: Byte): HyperSmooth {
    return when {
        (data == (GoProBleCommands.SetHyperSmoothOff.byteArray.last())) -> HyperSmooth.OFF
        (data == (GoProBleCommands.SetHyperSmoothLow.byteArray.last())) -> HyperSmooth.LOW
        (data == (GoProBleCommands.SetHyperSmoothHigh.byteArray.last())) -> HyperSmooth.HIGH
        (data == (GoProBleCommands.SetHyperSmoothBoost.byteArray.last())) -> HyperSmooth.BOOST
        (data == (GoProBleCommands.SetHyperSmoothAuto.byteArray.last())) -> HyperSmooth.AUTO
        (data == (GoProBleCommands.SetHyperSmoothStandard.byteArray.last())) -> HyperSmooth.STANDARD
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

internal fun mapHyperSmoothToMessage(hyperSmooth: HyperSmooth): ByteArray = when (hyperSmooth) {
    HyperSmooth.OFF -> GoProBleCommands.SetHyperSmoothOff.byteArray
    HyperSmooth.LOW -> GoProBleCommands.SetHyperSmoothLow.byteArray
    HyperSmooth.HIGH -> GoProBleCommands.SetHyperSmoothHigh.byteArray
    HyperSmooth.BOOST -> GoProBleCommands.SetHyperSmoothBoost.byteArray
    HyperSmooth.AUTO -> GoProBleCommands.SetHyperSmoothAuto.byteArray
    HyperSmooth.STANDARD -> GoProBleCommands.SetHyperSmoothStandard.byteArray
}

internal fun mapPresets(data: Byte): Presets {
    return when {
        (data == (GoProBleCommands.SetPresetsPhoto.byteArray.last())) -> Presets.PHOTO
        (data == (GoProBleCommands.SetPresetsVideo.byteArray.last())) -> Presets.VIDEO
        (data == (GoProBleCommands.SetPresetsTimeLapse.byteArray.last())) -> Presets.TIME_LAPSE
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

internal fun mapSpeed(data: Byte): Speed {
    return when {
        (data == (GoProBleCommands.SetSpeedNormal.byteArray.last())) -> Speed.REGULAR_1X
        (data == (GoProBleCommands.SetSpeedSlow.byteArray.last())) -> Speed.SLOW_MO_2X
        (data == (GoProBleCommands.SetSpeedSuperSLowMo.byteArray.last())) -> Speed.SUPER_SLOW_MO_4X
        (data == (GoProBleCommands.SetSpeedUltraSlowMo.byteArray.last())) -> Speed.ULTRA_SLOW_MO_8X
        (data == (GoProBleCommands.SetSpeedSlowMoLongBattery.byteArray.last())) -> Speed.SLOW_MO_LONG_BATTERY_2X
        (data == (GoProBleCommands.SetSpeedSuperSlowMoLongBattery.byteArray.last())) -> Speed.SUPER_SLOW_MO_LONG_BATTERY_4X
        (data == (GoProBleCommands.SetSpeedUltraSlowMoLongBattery.byteArray.last())) -> Speed.ULTRA_SLOW_MO_LONG_BATTERY_8X
        (data == (GoProBleCommands.SetSpeedSlow4K.byteArray.last())) -> Speed.SLOW_MO_2X_4K
        (data == (GoProBleCommands.SetSpeedSuperSLowMo27k.byteArray.last())) -> Speed.SUPER_SLOW_MO_4X_17K
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

internal fun mapSpeedToMessage(speed: Speed): ByteArray = when (speed) {
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

internal fun mapShutters(data: Byte): Boolean {
    return data == 0x01.toByte()
}

internal fun mapShuttersToMessage(isActivated: Boolean): ByteArray = when (isActivated) {
    true -> GoProBleCommands.SetShutterOn.byteArray
    false -> GoProBleCommands.SetShutterOff.byteArray
}

internal fun mapGoProSettings(byteArray: ByteArray): GoProSettings {
    if (byteArray.size < 3) {
        throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
    }

    return when (byteArray[2]) {
        RESOLUTION_CHARACTERISTIC_ID -> GoProSettings.ResolutionSettings(mapResolution(byteArray.last()))
        FRAME_RATE_CHARACTERISTIC_ID -> GoProSettings.FrameRateSettings(mapFrameRate(byteArray.last()))
        HYPER_SMOOTH_CHARACTERISTIC_ID -> GoProSettings.HyperSmoothSettings(mapHyperSmooth(byteArray.last()))
        SPEED_CHARACTERISTIC_ID -> GoProSettings.SpeedSettings(mapSpeed(byteArray.last()))
        SHUTTER_CHARACTERISTIC_ID -> GoProSettings.ShuttersSettings(mapShutters(byteArray.last()))
        PRESETS_CHARACTERISTIC_ID -> GoProSettings.PresetsSettings(mapPresets(byteArray.last()))
        else -> GoProSettings.NotSupported(byteArray)
    }
}