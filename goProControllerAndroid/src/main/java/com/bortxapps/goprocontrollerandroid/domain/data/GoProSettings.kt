package com.bortxapps.goprocontrollerandroid.domain.data

sealed class GoProSettings {
    data class SpeedSettings(val speed: Speed) : GoProSettings()
    data class ResolutionSettings(val resolution: Resolution) : GoProSettings()
    data class FrameRateSettings(val frameRate: FrameRate) : GoProSettings()
    data class HyperSmoothSettings(val hyperSmooth: HyperSmooth) : GoProSettings()
    data class PresetsSettings(val presets: Presets) : GoProSettings()
    data class ShuttersSettings(val isActivated: Boolean) : GoProSettings()
    data class NotSupported(val raw: ByteArray) : GoProSettings()
}