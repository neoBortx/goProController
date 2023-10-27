package com.bortxapps.goprocontrollerexample.screens.cameralist

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.domain.data.PairedState
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

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

    private fun getCamerasNearby() {
        Log.d("ExampleViewModel", "getCamerasNearby")
        val nearbyCameras = mutableStateListOf<GoProCamera>()


        _state.value = CameraListScreenState.Loading(nearbyCameras)

        val paired = goProController.getCamerasPaired()


        goProController.getNearByCameras()
            .onStart { _state.value = CameraListScreenState.Loading(nearbyCameras) }
            .filter { camera -> nearbyCameras.none { it.address == camera.address } }
            .map { camera -> camera.copy(pairedState = mapToPairedState(camera, paired)) }
            .onEach { camera -> _state.value = CameraListScreenState.Loading(nearbyCameras.apply { add(camera) }) }
            .onCompletion { processOnCompletion(nearbyCameras) }
            .catch { error -> processError(error) }
            .launchIn(viewModelScope)
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
            _state.value = CameraListScreenState.EmptyList
        } else {
            _state.value = CameraListScreenState.Finished(nearbyCameras)
        }
    }


    fun checkPermissions() = goProController.checkPermissions()
}