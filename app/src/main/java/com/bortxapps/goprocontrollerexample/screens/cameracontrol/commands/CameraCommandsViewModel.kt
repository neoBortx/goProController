package com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
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
        Log.d("CameraCommandsViewModel", "getInitialData")
        updateState()
    }

    private fun handleIntent(intent: CameraCommandsScreenIntent) {
        when (intent) {
            is CameraCommandsScreenIntent.ChangePresets -> {
                Log.d("CameraCommandsViewModel", "setPresets")
                setPresets(intent.presets)
            }

            is CameraCommandsScreenIntent.ChangeFrameRate -> {
                Log.d("CameraCommandsViewModel", "SetVideoFrameRate")
                setVideoFrameRate(intent.frameRate)
            }

            is CameraCommandsScreenIntent.ChangeResolution -> {
                Log.d("CameraCommandsViewModel", "SetResolution")
                setResolution(intent.resolution)
            }

            is CameraCommandsScreenIntent.ChangeHyperSmooth -> {
                Log.d("CameraCommandsViewModel", "SetHyperSmooth")
                setHyperSmooth(intent.hyperSmooth)
            }

            is CameraCommandsScreenIntent.ChangeSpeed -> {
                Log.d("CameraCommandsViewModel", "SetSpeed")
                setSpeed(intent.speed)
            }

            CameraCommandsScreenIntent.ShutterClicked -> {
                Log.d("CameraCommandsViewModel", "ShutterClicked")
                pressShutter()
            }
        }
    }

    private fun updateState(shutterOn: Boolean = false) {
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
                            speed,
                            shutterOn
                        )
                    }
                } catch (ex: GoProException) {
                    _state.value = CameraCommandsScreenState.Error(error = ex.message)
                } catch (ex: Exception) {
                    _state.value = CameraCommandsScreenState.Error("UNKNOWN")
                }
            }
        }
    }

    private fun notifyCommandFailed() {
        _state.update {
            (_state.value as CameraCommandsScreenState.StateRetrieved).copy(lastCommandRejected = true)
        }
    }

    private fun setPresets(presets: Presets) = viewModelScope.launch {
        withContext(IO) {
            when (presets) {
                Presets.VIDEO -> {
                    goProController.setPresetsVideo().fold({
                        updateState()
                    }, {
                        Log.e("CameraCommandsViewModel", "setCameraVideoPresets -> $it")
                        notifyCommandFailed()
                    })
                }

                Presets.PHOTO -> {
                    goProController.setPresetsPhoto().fold({
                        updateState()
                    }, {
                        Log.e("CameraCommandsViewModel", "MutableStateFlow -> $it")
                        notifyCommandFailed()
                    })
                }

                Presets.TIME_LAPSE -> {
                    goProController.setPresetsTimeLapse().fold({
                        updateState()
                    }, {
                        Log.e("CameraCommandsViewModel", "MutableStateFlow -> $it")
                        notifyCommandFailed()
                    })
                }
            }

        }
    }

    private fun setVideoFrameRate(frameRate: FrameRate) = viewModelScope.launch {
        withContext(IO) {
            goProController.setFrameRate(frameRate).fold({
                updateState()
            }, {
                Log.e("CameraCommandsViewModel", "setVideoFrameRate -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun setResolution(res: Resolution) = viewModelScope.launch {
        withContext(IO) {
            goProController.setResolution(res).fold({
                updateState()
            }, {
                Log.e("CameraCommandsViewModel", "setResolution -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun setHyperSmooth(hyperSmooth: HyperSmooth) = viewModelScope.launch {
        withContext(IO) {
            goProController.setHyperSmooth(hyperSmooth).fold({
                updateState()
            }, {
                Log.e("CameraCommandsViewModel", "setHyperSmooth -> $it")
                notifyCommandFailed()
            })
        }
    }


    private fun setSpeed(speed: Speed) = viewModelScope.launch {
        withContext(IO) {
            goProController.setSpeed(speed).fold({
                updateState()
            }, {
                Log.e("CameraCommandsViewModel", "setSpeed -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun pressShutter() = viewModelScope.launch {
        withContext(IO) {
            goProController.setShutterOn().fold({
                updateState(true)
                releaseShutter()
            }, {
                Log.e("CameraCommandsViewModel", "setShutterOn -> $it")
                notifyCommandFailed()
            })
        }
    }

    private fun releaseShutter() = viewModelScope.launch {
        withContext(IO) {
            goProController.setShutterOff().fold({
                updateState(false)
            }, {
                Log.e("CameraCommandsViewModel", "setShutterOff -> $it")
                notifyCommandFailed()
            })
        }
    }
}