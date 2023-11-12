package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed

sealed class CameraMediaScreenIntent {
    data class ChangeResolution(val resolution: Resolution) : CameraMediaScreenIntent()
    data class ChangeFrameRate(val frameRate: FrameRate) : CameraMediaScreenIntent()
    data class ChangeHyperSmooth(val hyperSmooth: HyperSmooth) : CameraMediaScreenIntent()
    data class ChangeSpeed(val speed: Speed) : CameraMediaScreenIntent()
    data class ChangePresets(val presets: Presets) : CameraMediaScreenIntent()
    object ShutterClicked : CameraMediaScreenIntent()

}