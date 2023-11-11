package com.bortxapps.goprocontrollerexample.screens.cameralist

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.domain.data.PairedState
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerexample.screens.cameralist.intent.CameraListScreenIntent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraListViewModel(private val goProController: GoProController) : ViewModel() {

    val intentChannel: Channel<CameraListScreenIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CameraListScreenState>(CameraListScreenState.Loading(emptyList()))
    val state: StateFlow<CameraListScreenState>
        get() = _state

    init {
        viewModelScope.launch {
            intentChannel.consumeAsFlow().collect {
                handleIntent(it)
            }
        }
    }

    private fun handleIntent(intent: CameraListScreenIntent) {
        when (intent) {
            is CameraListScreenIntent.SearchCameras -> {
                Log.d("ExampleViewModel", "SearchCameras")
                getCamerasNearby()
            }
        }
    }

    private fun getCamerasNearby() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            Log.d("ExampleViewModel", "getCamerasNearby")
            val nearbyCameras = mutableStateListOf<GoProCamera>()

            _state.value = CameraListScreenState.Loading(nearbyCameras)

            val paired = goProController.getCamerasPaired().getOrNull() ?: emptyList()

            goProController.getNearByCameras().onSuccess {
                it.onStart { _state.update { CameraListScreenState.Loading(nearbyCameras) } }
                    .filter { camera -> nearbyCameras.none { nearCamera -> nearCamera.address == camera.address } }
                    .map { camera -> camera.copy(pairedState = mapToPairedState(camera, paired)) }
                    .onEach { cam -> _state.update { CameraListScreenState.Loading(nearbyCameras.apply { add(cam) }) } }
                    .onCompletion { processOnCompletion(nearbyCameras) }
                    .catch { error -> processError(error) }
                    .launchIn(viewModelScope)
            }.onFailure {
                _state.update { CameraListScreenState.Error }
            }

        }
    }

    private fun mapToPairedState(nearbyCamera: GoProCamera, paired: List<GoProCamera>): PairedState {
        return if (nearbyCamera.address in paired.map { it.address }) {
            PairedState.PAIRED_LOCAL
        } else {
            PairedState.PAIRED_OTHER
        }
    }

    private fun processError(error: Throwable) {
        _state.value = CameraListScreenState.Error
        Log.e("ExampleViewModel", "Error -> $error")
    }

    private fun processOnCompletion(nearbyCameras: List<GoProCamera>) {
        if (nearbyCameras.isEmpty()) {
            _state.update { CameraListScreenState.EmptyList }
        } else {
            _state.update { CameraListScreenState.Finished(nearbyCameras) }
        }
    }
}