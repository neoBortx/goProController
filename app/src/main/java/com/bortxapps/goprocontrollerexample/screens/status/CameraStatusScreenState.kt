package com.bortxapps.goprocontrollerexample.screens.status

sealed class CameraStatusScreenState {

    object Loading : CameraStatusScreenState()
    object Error : CameraStatusScreenState()
    data class StateRetrieved(
        val cameraApiVersion: String,
        val map: Map<String, String>) : CameraStatusScreenState()
}