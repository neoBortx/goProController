package com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands.intent.CameraCommandsScreenIntent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraCommandsViewModel(private val goProController: GoProController) : ViewModel() {

    val intentChannel: Channel<CameraCommandsScreenIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CameraCommandsScreenState>(CameraCommandsScreenState.Loading)
    val state: StateFlow<CameraCommandsScreenState>
        get() = _state

    init {
        getInitialData()
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                handleIntent(it)
            }
        }
    }

    private fun getInitialData() {
        Log.d("ExampleViewModel", "getInitialData")
        updateState()
    }

    private fun handleIntent(intent: CameraCommandsScreenIntent) {
        when (intent) {
            is CameraCommandsScreenIntent.ChangePresets -> {
                when (intent.presets) {
                    Presets.VIDEO -> setCameraVideoPresets()
                    Presets.PHOTO -> setPhotoPresets()
                    Presets.TIME_LAPSE -> setTimeLapsePresets()
                }
            }

            is CameraCommandsScreenIntent.ChangeFrameRate -> {
                Log.d("ExampleViewModel", "SetVideoFrameRate")
                setVideoFrameRate(intent.frameRate)
            }

            is CameraCommandsScreenIntent.ChangeResolution -> {
                Log.d("ExampleViewModel", "SetResolution")
                setResolution(intent.resolution)
            }

            is CameraCommandsScreenIntent.ChangeHyperSmooth -> {
                Log.d("ExampleViewModel", "SetHyperSmooth")
                setHyperSmooth(intent.hyperSmooth)
            }

            is CameraCommandsScreenIntent.ChangeSpeed -> {
                Log.d("ExampleViewModel", "SetSpeed")
                setSpeed(intent.speed)
            }
        }
    }

    private fun updateState() {
        viewModelScope.launch {
            withContext(IO) {
                try {
                    val presets: Presets = goProController.getPresets().getOrThrow()
                    val resolution: Resolution = goProController.getResolution().getOrThrow()
                    val frameRate: FrameRate = goProController.getFrameRate().getOrThrow()
                    val hyperSmooth: HyperSmooth = goProController.getHyperSmooth().getOrThrow()
                    val speed: Speed = goProController.getSpeed().getOrThrow()
                    _state.update {
                        CameraCommandsScreenState.StateRetrieved(
                            resolution,
                            frameRate,
                            hyperSmooth,
                            presets,
                            speed
                        )
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                    _state.value = CameraCommandsScreenState.Error
                }
            }
        }
    }

    private fun notifyCommandFailed() {
        _state.update {
            (_state.value as CameraCommandsScreenState.StateRetrieved).copy(lastCommandRejected = true)
        }
    }

    private fun setCameraVideoPresets() = viewModelScope.launch {
        withContext(IO) {
            goProController.setPresetsVideo().fold({
                updateState()
            }, {
                Log.e("ExampleViewModel", "setCameraVideoPresets -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun setTimeLapsePresets() = viewModelScope.launch {
        withContext(IO) {
            goProController.setPresetsTimeLapse().fold({
                updateState()
            }, {
                Log.e("ExampleViewModel", "MutableStateFlow -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun setPhotoPresets() = viewModelScope.launch {
        withContext(IO) {
            goProController.setPresetsPhoto().fold({
                updateState()
            }, {
                Log.e("ExampleViewModel", "MutableStateFlow -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun setVideoFrameRate(frameRate: FrameRate) = viewModelScope.launch {
        withContext(IO) {
            goProController.setFrameRate(frameRate).fold({
                updateState()
            }, {
                Log.e("ExampleViewModel", "setVideoFrameRate -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun setResolution(res: Resolution) = viewModelScope.launch {
        withContext(IO) {
            goProController.setResolution(res).fold({
                updateState()
            }, {
                Log.e("ExampleViewModel", "setResolution -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun setHyperSmooth(hyperSmooth: HyperSmooth) = viewModelScope.launch {
        withContext(IO) {
            goProController.setHyperSmooth(hyperSmooth).fold({
                updateState()
            }, {
                Log.e("ExampleViewModel", "setHyperSmooth -> $it")
                notifyCommandFailed()
            })
        }
    }


    private fun setSpeed(speed: Speed) = viewModelScope.launch {
        withContext(IO) {
            goProController.setSpeed(speed).fold({
                updateState()
            }, {
                Log.e("ExampleViewModel", "setSpeed -> $it")
                notifyCommandFailed()
            })
        }
    }
}