package com.bortxapps.goprocontrollerexample.screens.commands

sealed class CameraCommandsScreenState {

    object Loading : CameraCommandsScreenState()
    object Error : CameraCommandsScreenState()
    data class StateRetrieved(
        val cameraApiVersion: String,
        val map: Map<String, String>) : CameraCommandsScreenState()
}