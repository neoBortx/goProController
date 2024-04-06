package com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.domain.data.GoProSettings
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands.intent.CameraCommandsScreenIntent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class CameraCommandsViewModel(private val goProController: GoProController) : ViewModel() {

    val intentChannel: Channel<CameraCommandsScreenIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CameraCommandsScreenState>(CameraCommandsScreenState.Loading)
    val state: StateFlow<CameraCommandsScreenState>
        get() = _state

    init {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                handleIntent(it)
            }
        }
        viewModelScope.launch {
            withContext(IO) {
                getInitialData()
                subscribeToGoProEvents()
            }
        }
    }

    private suspend fun getInitialData() {
        Log.d("CameraCommandsViewModel", "getInitialData")
        withContext(IO) {
            try {
                val presets: Presets = goProController.getPresets().getOrThrow()
                val resolution: Resolution = goProController.getResolution().getOrThrow()
                val frameRate: FrameRate = goProController.getFrameRate().getOrThrow()
                val hyperSmooth: HyperSmooth = goProController.getHyperSmooth().getOrThrow()
                val speed: Speed = goProController.getSpeed().getOrThrow()
                val shutterOn = false
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

    private suspend fun subscribeToGoProEvents() {
        goProController.subscribeToCameraSettingsChanges().collect {
            when (it) {
                is GoProSettings.SpeedSettings -> {
                    Log.d("CameraCommandsViewModel", "SpeedSettings has changed ${it.speed}")
                    _state.update { currentState ->
                        (currentState as CameraCommandsScreenState.StateRetrieved).copy(speed = it.speed)
                    }
                }

                is GoProSettings.ResolutionSettings -> {
                    Log.d("CameraCommandsViewModel", "ResolutionSettings has changed ${it.resolution}")
                    _state.update { currentState ->
                        (currentState as CameraCommandsScreenState.StateRetrieved).copy(resolution = it.resolution)
                    }
                }

                is GoProSettings.FrameRateSettings -> {
                    Log.d("CameraCommandsViewModel", "FrameRateSettings has changed ${it.frameRate}")
                    _state.update { currentState ->
                        (currentState as CameraCommandsScreenState.StateRetrieved).copy(frameRate = it.frameRate)
                    }
                }

                is GoProSettings.HyperSmoothSettings -> {
                    Log.d("CameraCommandsViewModel", "HyperSmoothSettings has changed ${it.hyperSmooth}")
                    _state.update { currentState ->
                        (currentState as CameraCommandsScreenState.StateRetrieved).copy(hyperSmooth = it.hyperSmooth)
                    }
                }

                is GoProSettings.PresetsSettings -> {
                    Log.d("CameraCommandsViewModel", "PresetsSettings has changed ${it.presets}")
                    _state.update { currentState ->
                        (currentState as CameraCommandsScreenState.StateRetrieved).copy(presets = it.presets)
                    }
                }

                is GoProSettings.ShuttersSettings -> {
                    Log.d("CameraCommandsViewModel", "ShuttersSettings has changed ${it.isActivated}")
                    _state.update { currentState ->
                        (currentState as CameraCommandsScreenState.StateRetrieved).copy(shutterOn = it.isActivated)
                    }
                }

                is GoProSettings.NotSupported -> {
                    Log.d(
                        "CameraCommandsViewModel",
                        "NotSupported -> ${it.raw.joinToString(separator = ":") { String.format(Locale.getDefault(), "%02X", it) }}"
                    )
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
                    goProController.setPresetsVideo().fold(
                        onSuccess = {
                            Log.d("CameraCommandsViewModel", "setPresetsVideo success")
                            goProController.getPresets()
                        },
                        onFailure = {
                            notifyCommandFailed()
                        }
                    )
                }

                Presets.PHOTO -> {
                    goProController.setPresetsPhoto().fold(
                        onSuccess = {
                            Log.d("CameraCommandsViewModel", "setPresetsPhoto success")
                            goProController.getPresets()
                        },
                        onFailure = {
                            notifyCommandFailed()
                        }
                    )
                }

                Presets.TIME_LAPSE -> {
                    goProController.setPresetsTimeLapse().fold(
                        onSuccess = {
                            Log.d("CameraCommandsViewModel", "setPresetsTimeLapse success")
                            goProController.getPresets()
                        },
                        onFailure = {
                            notifyCommandFailed()
                        }
                    )
                }
            }

        }
    }

    private fun setVideoFrameRate(frameRate: FrameRate) = viewModelScope.launch {
        withContext(IO) {
            goProController.setFrameRate(frameRate).fold(
                onSuccess = {
                    Log.d("CameraCommandsViewModel", "setFrameRate success")
                    goProController.getFrameRate()
                },
                onFailure = {
                    notifyCommandFailed()
                }
            )
        }
    }

    private fun setResolution(res: Resolution) = viewModelScope.launch {
        withContext(IO) {
            goProController.setResolution(res).fold(
                onSuccess = {
                    Log.d("CameraCommandsViewModel", "setResolution success")
                    goProController.getResolution()
                },
                onFailure = {
                    notifyCommandFailed()
                }
            )
        }
    }

    private fun setHyperSmooth(hyperSmooth: HyperSmooth) = viewModelScope.launch {
        withContext(IO) {
            goProController.setHyperSmooth(hyperSmooth).fold(
                onSuccess = {
                    Log.d("CameraCommandsViewModel", "setHyperSmooth success")
                    goProController.getHyperSmooth()
                },
                onFailure = {
                    notifyCommandFailed()
                }
            )
        }
    }


    private fun setSpeed(speed: Speed) = viewModelScope.launch {
        withContext(IO) {
            goProController.setSpeed(speed).fold(
                onSuccess = {
                    Log.d("CameraCommandsViewModel", "setSpeed success")
                    goProController.getSpeed()
                },
                onFailure = {
                    notifyCommandFailed()
                }
            )
        }
    }

    private fun pressShutter() = viewModelScope.launch {
        withContext(IO) {
            goProController.setShutterOn().fold(
                onSuccess = {
                    Log.d("CameraCommandsViewModel", "setShutterOn success")
                },
                onFailure = {
                    notifyCommandFailed()
                }
            )
        }
    }

    private fun releaseShutter() = viewModelScope.launch {
        withContext(IO) {
            goProController.setShutterOff().exceptionOrNull()?.let {
                notifyCommandFailed()
            }
        }
    }
}