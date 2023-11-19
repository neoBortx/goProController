package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItemType
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiStatus
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CameraMediaViewModel(private val goProController: GoProController) : ViewModel() {

    private val _state = MutableStateFlow<CameraMediaScreenState>(CameraMediaScreenState.Loading)
    val state: StateFlow<CameraMediaScreenState>
        get() = _state

    private val _stateDialog = MutableStateFlow<CameraMediaScreenDialogState>(CameraMediaScreenDialogState.Idle)
    val stateDialog: StateFlow<CameraMediaScreenDialogState>
        get() = _stateDialog

    init {
        connect()
    }

    private fun connect() = viewModelScope.launch {
        withContext(IO) {
            val wifiEnabledInCamera = goProController.enableWifiInMobile()
            val wifiEnabledInMobile = goProController.enableWifiAp()
            val wifiEnabled = wifiEnabledInCamera.isSuccess && wifiEnabledInMobile.isSuccess

            val wifiSSID = goProController.getWifiApSSID()
            val wifiPassword = goProController.getWifiApPassword()

            if (wifiEnabled
                && wifiSSID.isSuccess
                && wifiPassword.isSuccess
            ) {
                goProController.connectToWifi(wifiSSID.getOrElse { "" }, wifiPassword.getOrElse { "" }).collect {
                    it.fold({ wifiStatus ->
                        processState(wifiStatus)
                    }, { error ->
                        _state.update { CameraMediaScreenState.Error(error.message.orEmpty()) }
                    })
                }
            } else {
                _state.update { CameraMediaScreenState.Error("UNABLE TO RETRIEVE SSID AND WIFI PASSWORD") }
            }
        }
    }

    fun onItemClicked(goProMediaItem: GoProMediaItem) {
        when (goProMediaItem.mediaType) {
            GoProMediaItemType.SINGLE_PHOTO -> {
                loadImage(goProMediaItem)
            }

            GoProMediaItemType.VIDEO,
            GoProMediaItemType.CHAPTERED_VIDEO -> {
                loadVideo(goProMediaItem)
            }

            else -> {}
        }
    }

    fun onDismissDialog() {
        _stateDialog.update { CameraMediaScreenDialogState.Idle }
    }

    private fun processState(wfiStatus: WifiStatus) {
        when (wfiStatus) {
            WifiStatus.CONNECTING -> _state.update { CameraMediaScreenState.Loading }
            WifiStatus.CONNECTED -> getMedia()
            WifiStatus.CONNECTION_FAILED -> _state.update { CameraMediaScreenState.Error("CONNECTION FAILED") }
            WifiStatus.CONNECTION_LOST -> _state.update { CameraMediaScreenState.Error("CONNECTION LOST") }
        }
    }

    private fun getMedia() = viewModelScope.launch {
        withContext(IO) {
            _state.update { CameraMediaScreenState.Loading }
            goProController.getMediaList().fold({ items ->
                _state.update { CameraMediaScreenState.MediaList(items) }
            }, { error ->
                _state.update { CameraMediaScreenState.Error(error.message.orEmpty()) }
            })
        }
    }

    suspend fun onLoadThumbnail(s: String): ByteArray? = goProController.getMediaThumbnail(s).getOrElse { null }

    private fun loadVideo(goProMediaItem: GoProMediaItem) = viewModelScope.launch {
        transitionToRetrievedVideoFile(goProMediaItem)
    }

    private fun loadImage(goProMediaItem: GoProMediaItem) = viewModelScope.launch {
        transitionToRetrievedImageFile(goProMediaItem)
    }

    private fun transitionToRetrievedImageFile(goProMediaItem: GoProMediaItem) {
        _stateDialog.update { CameraMediaScreenDialogState.RetrievedImageFile(goProMediaItem) }
    }

    private fun transitionToRetrievedVideoFile(goProMediaItem: GoProMediaItem) {
        _stateDialog.update { CameraMediaScreenDialogState.RetrievedVideoFile(goProMediaItem) }
    }
}