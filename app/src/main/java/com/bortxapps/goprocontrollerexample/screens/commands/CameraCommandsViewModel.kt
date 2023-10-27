package com.bortxapps.goprocontrollerexample.screens.commands

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerandroid.feature.QUERY_PARAM_SETTING_FRAME_RATE
import com.bortxapps.goprocontrollerandroid.feature.QUER_PARAM_SETTINGS_RESOLUTION
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraCommandsViewModel(private val goProController: GoProController, val savedStateHandle: SavedStateHandle) : ViewModel() {

    fun getCameraPresets() = viewModelScope.launch {
        Log.d("ExampleViewModel", "getCameraPresets")
        withContext(IO) {
            goProController.getCameraPresets().onRight {
                Log.d("ExampleViewModel", "getCameraPresets -> $it")
            }.onLeft {
                Log.e("ExampleViewModel", "getCameraPresets -> $it")
            }
        }
    }

    fun setCameraVideoPresets() = viewModelScope.launch {
        Log.d("ExampleViewModel", "setCameraVideoPresets")
        withContext(IO) {
            goProController.setCameraVideoPresets().onRight {
                Log.d("ExampleViewModel", "setCameraVideoPresets -> $it")
                getCameraPresets()
            }.onLeft {
                Log.e("ExampleViewModel", "setCameraVideoPresets -> $it")
            }
        }
    }

    fun setCameraTimeLapsePresets() = viewModelScope.launch {
        Log.d("ExampleViewModel", "setCameraTimeLapsePresets")
        withContext(IO) {
            goProController.setCameraTimeLapsePresets().onRight {
                Log.d("ExampleViewModel", "setCameraTimeLapsePresets -> $it")
                getCameraPresets()
            }.onLeft {
                Log.e("ExampleViewModel", "setCameraTimeLapsePresets -> $it")
            }
        }
    }

    fun setVideoFrameRate(frameRate: QUERY_PARAM_SETTING_FRAME_RATE) = viewModelScope.launch {
        Log.d("ExampleViewModel", "setVideoFrameRate")
        withContext(IO) {
            goProController.setFrameRate(frameRate).onRight {
                Log.d("ExampleViewModel", "setVideoFrameRate -> $it")
                getCameraPresets()
            }.onLeft {
                Log.e("ExampleViewModel", "setVideoFrameRate -> $it")
            }
        }
    }

    fun setResolution(res: QUER_PARAM_SETTINGS_RESOLUTION) = viewModelScope.launch {
        Log.d("ExampleViewModel", "setResolution")
        withContext(IO) {
            goProController.setResolution(res).onRight {
                Log.d("ExampleViewModel", "setResolution -> $it")
            }.onLeft {
                Log.e("ExampleViewModel", "setResolution -> $it")
            }
        }
    }




}