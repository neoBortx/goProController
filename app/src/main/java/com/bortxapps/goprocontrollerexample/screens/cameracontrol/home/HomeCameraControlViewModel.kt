package com.bortxapps.goprocontrollerexample.screens.cameracontrol.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeCameraControlViewModel(val goProController: GoProController, val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state: MutableStateFlow<CameraHomeScreenState> = MutableStateFlow(CameraHomeScreenState.Loading)
    val state: StateFlow<CameraHomeScreenState>
        get() = _state

    init {
        val address = savedStateHandle.get<String>("address").orEmpty()
        connectToDevice(address)
    }

    private fun connectToDevice(address: String) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            goProController.stopSearch()
            Log.d("ExampleViewModel", "connectToDevice")
            goProController.connectToDevice(address).fold(
                {
                    Log.d("ExampleViewModel", "paired to : $address")
                    _state.value = CameraHomeScreenState.Connected
                },
                {
                    Log.e("ExampleViewModel", "error pairing to : $address")
                    _state.value = CameraHomeScreenState.Error
                }
            )
        }
    }
}