package com.bortxapps.goprocontrollerexample.screens.cameralist

import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera

sealed class CameraListScreenState {
    object Error : CameraListScreenState()
    object EmptyList : CameraListScreenState()
    data class Loading(val cameras: List<GoProCamera>) : CameraListScreenState()
    data class Finished(val cameras: List<GoProCamera>) : CameraListScreenState()
}