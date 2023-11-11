package com.bortxapps.goprocontrollerexample.screens.cameracontrol.home

sealed class CameraHomeScreenState {

    object Loading : CameraHomeScreenState()
    object Error : CameraHomeScreenState()
    object Connected : CameraHomeScreenState()
}