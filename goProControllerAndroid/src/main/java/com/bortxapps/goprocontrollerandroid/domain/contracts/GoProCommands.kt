package com.bortxapps.goprocontrollerandroid.domain.contracts

import arrow.core.Either
import com.bortxapps.goprocontrollerandroid.domain.GoProError
import com.bortxapps.goprocontrollerandroid.feature.QUERY_PARAM_SETTING_FRAME_RATE
import com.bortxapps.goprocontrollerandroid.feature.QUER_PARAM_SETTINGS_RESOLUTION
import java.util.Date

interface GoProCommands {
    suspend fun getCameraState(): Either<GoProError, Map<String, String>>

    suspend fun setDigitalZoom(zoom: Int): Either<GoProError, String>

    suspend fun getDateTime(): Either<GoProError, String>

    suspend fun setDateTime(date: Date): Either<GoProError, String>

    suspend fun getKeepAlive(): Either<GoProError, String>

    suspend fun startCameraStream(): Either<GoProError, String>

    suspend fun stopCameraStream(): Either<GoProError, String>

    suspend fun startShutterStream(): Either<GoProError, String>

    suspend fun stopShutterStream(): Either<GoProError, String>

    suspend fun getCameraPresets(): Either<GoProError, String>

    suspend fun setCameraVideoPresets(): Either<GoProError, String>

    suspend fun setCameraPhotoPresets(): Either<GoProError, String>

    suspend fun setCameraTimeLapsePresets(): Either<GoProError, String>

    suspend fun setFrameRate(frameRate: QUERY_PARAM_SETTING_FRAME_RATE): Either<GoProError, String>

    suspend fun setResolution(res: QUER_PARAM_SETTINGS_RESOLUTION): Either<GoProError, String>
}