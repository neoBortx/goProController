package com.bortxapps.goprocontrollerexample.screens.status

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerexample.screens.cameralist.CameraListScreenState
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraStatusViewModel(val goProController: GoProController, val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _state: MutableStateFlow<CameraStatusScreenState> = MutableStateFlow(CameraStatusScreenState.Loading)
    private var stateRetrieved = CameraStatusScreenState.StateRetrieved("", mapOf())

    val state: StateFlow<CameraStatusScreenState>
        get() = _state


    init {
        val address = savedStateHandle.get<String>("address").orEmpty()
        connectToDevice(address)
    }

    private fun connectToDevice(address: String) = viewModelScope.launch {
        withContext(IO) {
            goProController.stopSearch()
            Log.d("ExampleViewModel", "connectToDevice")
            goProController.connectToDevice(address).fold(
                {
                    Log.e("ExampleViewModel", "Error -> $it")
                    _state.value = CameraStatusScreenState.Error
                },
                {
                    Log.d("ExampleViewModel", "paired to : $address")
                    getCameraState()
                    getCameraApiVersion()
                }
            )
        }
    }

    private fun getCameraApiVersion() = viewModelScope.launch {
        Log.d("ExampleViewModel", "getCameraApiVersion")
        withContext(IO) {
            goProController.getOpenGoProVersion().onRight {
                stateRetrieved = stateRetrieved.copy(cameraApiVersion = it)
                _state.value = stateRetrieved
            }.onLeft {
                _state.value = CameraStatusScreenState.Error
            }
        }
    }

    private fun getCameraState() = viewModelScope.launch {
        Log.d("ExampleViewModel", "getCameraState")
        withContext(IO) {
            goProController.getCameraStatus().onRight {
                stateRetrieved = stateRetrieved.copy(map = it)
                _state.value = stateRetrieved
            }.onLeft {
                _state.value = CameraStatusScreenState.Error
            }
        }
    }


}