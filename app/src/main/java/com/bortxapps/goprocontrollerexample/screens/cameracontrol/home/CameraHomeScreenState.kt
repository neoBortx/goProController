package com.bortxapps.goprocontrollerexample.screens.cameracontrol.home

sealed class CameraHomeScreenState {

    object Loading : CameraHomeScreenState()
    data class Error(val error: String) : CameraHomeScreenState()
    object Connected : CameraHomeScreenState()
}