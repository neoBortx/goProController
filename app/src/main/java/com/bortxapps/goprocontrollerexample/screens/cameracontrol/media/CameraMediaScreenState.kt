package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media

import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem

sealed class CameraMediaScreenState {
    object Loading : CameraMediaScreenState()
    data class Error(val error: String) : CameraMediaScreenState()
    data class MediaList(val items: List<GoProMediaItem>) : CameraMediaScreenState()
}