package com.bortxapps.goprocontrollerexample.screens.cameracontrol.media

import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem

sealed class CameraMediaScreenDialogState {
    object Idle : CameraMediaScreenDialogState()
    data class RetrievedImageFile(val goProMediaItem: GoProMediaItem) : CameraMediaScreenDialogState()
    data class RetrievedVideoFile(val goProMediaItem: GoProMediaItem) : CameraMediaScreenDialogState()

    data class RetrievedGroupImageFile(val goProMediaItem: GoProMediaItem) : CameraMediaScreenDialogState()

}