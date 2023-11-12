package com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed

sealed class CameraCommandsScreenState {

    object Loading : CameraCommandsScreenState()
    data class Error(val error: String) : CameraCommandsScreenState()
    data class StateRetrieved(
        val resolution: Resolution,
        val frameRate: FrameRate,
        val hyperSmooth: HyperSmooth,
        val presets: Presets,
        val speed: Speed,
        val lastCommandRejected: Boolean = false,
        val shutterOn: Boolean = false
    ) : CameraCommandsScreenState()
}