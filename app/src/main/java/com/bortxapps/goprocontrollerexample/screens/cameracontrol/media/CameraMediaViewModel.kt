package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bortxapps.goprocontrollerandroid.exposedapi.GoProController
import com.bortxapps.goprocontrollerandroid.feature.GET_THUMBNAIL_URL
import com.bortxapps.goprocontrollerandroid.feature.GOPRO_BASE_URL
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiStatus
import com.bortxapps.goprocontrollerexample.screens.cameracontrol.commands.intent.CameraCommandsScreenIntent
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class CameraMediaViewModel(val goProController: GoProController) : ViewModel() {

    val intentChannel: Channel<CameraCommandsScreenIntent> = Channel(Channel.UNLIMITED)

    private val _state = MutableStateFlow<CameraMediaScreenState>(CameraMediaScreenState.Loading)
    val state: StateFlow<CameraMediaScreenState>
        get() = _state

    init {
        connect()
    }

    fun connect() = viewModelScope.launch {
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

    fun processState(wfiStatus: WifiStatus) {
        when (wfiStatus) {
            WifiStatus.CONNECTING -> _state.update { CameraMediaScreenState.Loading }
            WifiStatus.CONNECTED -> getMedia()
            WifiStatus.CONNECTION_FAILED -> _state.update { CameraMediaScreenState.Error("CONNECTION FAILED") }
            WifiStatus.CONNECTION_LOST -> _state.update { CameraMediaScreenState.Error("CONNECTION LOST") }
        }
    }

    fun getMedia() = viewModelScope.launch {
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
}