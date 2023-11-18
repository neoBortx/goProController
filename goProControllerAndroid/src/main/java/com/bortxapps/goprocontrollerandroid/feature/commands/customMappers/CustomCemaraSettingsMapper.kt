package com.bortxapps.goprocontrollerandroid.feature.commands.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProBleCommands
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed

fun mapResolution(data: UByte): Resolution {
    return when {
        (data == (GoProBleCommands.SetResolution53K.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_5_3K
        (data == (GoProBleCommands.SetResolution4K.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_4K
        (data == (GoProBleCommands.SetResolution4K43.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_4K_4_3
        (data == (GoProBleCommands.SetResolution2K.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_2_7K
        (data == (GoProBleCommands.SetResolution2K43.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_2_7K_4_3
        (data == (GoProBleCommands.SetResolution1440.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_1440
        (data == (GoProBleCommands.SetResolution1080.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_1080
        (data == (GoProBleCommands.SetResolution5KGoPro12.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_5K_GOPRO_12
        (data == (GoProBleCommands.SetResolution4KGoPro12.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_4K_GOPRO_12
        (data == (GoProBleCommands.SetResolution4K43GoPro12.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_4K_4_3_GOPRO_12
        (data == (GoProBleCommands.SetResolution2KGoPro12.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_2K_GO_PRO_12
        (data == (GoProBleCommands.SetResolution2K43GoPro12.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_2K_4_3_GO_PRO_12
        (data == (GoProBleCommands.SetResolution1080GoPro12.byteArray.last()).toUByte()) -> Resolution.RESOLUTION_1080_GOPRO_12
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

fun mapFrameRate(data: UByte): FrameRate {
    return when {
        (data == (GoProBleCommands.SetFrameRate240.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_240
        (data == (GoProBleCommands.SetFrameRate200.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_200
        (data == (GoProBleCommands.SetFrameRate120.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_120
        (data == (GoProBleCommands.SetFrameRate100.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_100
        (data == (GoProBleCommands.SetFrameRate60.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_60
        (data == (GoProBleCommands.SetFrameRate50.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_50
        (data == (GoProBleCommands.SetFrameRate30.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_30
        (data == (GoProBleCommands.SetFrameRate25.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_25
        (data == (GoProBleCommands.SetFrameRate24.byteArray.last()).toUByte()) -> FrameRate.FRAME_RATE_24
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

fun mapHyperSmooth(data: UByte): HyperSmooth {
    return when {
        (data == (GoProBleCommands.SetHyperSmoothOff.byteArray.last()).toUByte()) -> HyperSmooth.OFF
        (data == (GoProBleCommands.SetHyperSmoothLow.byteArray.last()).toUByte()) -> HyperSmooth.LOW
        (data == (GoProBleCommands.SetHyperSmoothHigh.byteArray.last()).toUByte()) -> HyperSmooth.HIGH
        (data == (GoProBleCommands.SetHyperSmoothBoost.byteArray.last()).toUByte()) -> HyperSmooth.BOOST
        (data == (GoProBleCommands.SetHyperSmoothAuto.byteArray.last()).toUByte()) -> HyperSmooth.AUTO
        (data == (GoProBleCommands.SetHyperSmoothStandard.byteArray.last()).toUByte()) -> HyperSmooth.STANDARD
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

fun mapPresets(data: UByte): Presets {
    return when {
        (data == (GoProBleCommands.SetPresetsPhoto.byteArray.last()).toUByte()) -> Presets.PHOTO
        (data == (GoProBleCommands.SetPresetsVideo.byteArray.last()).toUByte()) -> Presets.VIDEO
        (data == (GoProBleCommands.SetPresetsTimeLapse.byteArray.last()).toUByte()) -> Presets.TIME_LAPSE
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}

fun mapSpeed(data: UByte): Speed {
    return when {
        (data == (GoProBleCommands.SetSpeedNormal.byteArray.last()).toUByte()) -> Speed.REGULAR_1X
        (data == (GoProBleCommands.SetSpeedSlow.byteArray.last()).toUByte()) -> Speed.SLOW_MO_2X
        (data == (GoProBleCommands.SetSpeedSuperSLowMo.byteArray.last()).toUByte()) -> Speed.SUPER_SLOW_MO_4X
        (data == (GoProBleCommands.SetSpeedUltraSlowMo.byteArray.last()).toUByte()) -> Speed.ULTRA_SLOW_MO_8X
        (data == (GoProBleCommands.SetSpeedSlowMoLongBattery.byteArray.last()).toUByte()) -> Speed.SLOW_MO_LONG_BATTERY_2X
        (data == (GoProBleCommands.SetSpeedSuperSlowMoLongBattery.byteArray.last()).toUByte()) -> Speed.SUPER_SLOW_MO_LONG_BATTERY_4X
        (data == (GoProBleCommands.SetSpeedUltraSlowMoLongBattery.byteArray.last()).toUByte()) -> Speed.ULTRA_SLOW_MO_LONG_BATTERY_8X
        (data == (GoProBleCommands.SetSpeedSlow4K.byteArray.last()).toUByte()) -> Speed.SLOW_MO_2X_4K
        (data == (GoProBleCommands.SetSpeedSuperSLowMo27k.byteArray.last()).toUByte()) -> Speed.SUPER_SLOW_MO_4X_17K
        else -> {
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}