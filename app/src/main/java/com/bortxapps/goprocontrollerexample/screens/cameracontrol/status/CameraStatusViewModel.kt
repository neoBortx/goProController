package com.bortxapps.goprocontrollerexample.screens.cameracontrol.status

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraStatusViewModel(private val goProController: GoProController) : ViewModel() {

    private val _state: MutableStateFlow<CameraStatusScreenState> = MutableStateFlow(CameraStatusScreenState.Loading)
    private var stateRetrieved = CameraStatusScreenState.StateRetrieved("", mapOf())

    val state: StateFlow<CameraStatusScreenState>
        get() = _state

    init {
        viewModelScope.launch {
            withContext(IO) {
                getCameraApiVersion()
                getCameraState()
            }
        }
    }

    private suspend fun getCameraApiVersion() {
        Log.d("ExampleViewModel", "getCameraApiVersion")
        goProController.getOpenGoProVersion().fold({
            stateRetrieved = stateRetrieved.copy(cameraApiVersion = it)
            _state.value = stateRetrieved
        }, {
            Log.e("ExampleViewModel", "getCameraApiVersion Error -> $it")
            _state.value = CameraStatusScreenState.Error(it.message.orEmpty())
        })
    }

    private suspend fun getCameraState() {
        Log.d("ExampleViewModel", "getCameraState")
        goProController.getCameraStatus().fold({
            stateRetrieved = stateRetrieved.copy(map = it)
            _state.value = stateRetrieved
        }, {
            Log.e("ExampleViewModel", "getCameraState Error -> $it")
            _state.value = CameraStatusScreenState.Error(it.message.orEmpty())
        })
    }
}