package com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands.intent

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed

sealed class CameraCommandsScreenIntent {
    data class ChangeResolution(val resolution: Resolution) : CameraCommandsScreenIntent()
    data class ChangeFrameRate(val frameRate: FrameRate) : CameraCommandsScreenIntent()
    data class ChangeHyperSmooth(val hyperSmooth: HyperSmooth) : CameraCommandsScreenIntent()
    data class ChangeSpeed(val speed: Speed) : CameraCommandsScreenIntent()
    data class ChangePresets(val presets: Presets) : CameraCommandsScreenIntent()
}