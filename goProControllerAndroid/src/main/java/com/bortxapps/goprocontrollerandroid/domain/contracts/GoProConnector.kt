package com.bortxapps.goprocontrollerandroid.domain.contracts

import android.content.Context
import arrow.core.Either
import com.bortxapps.goprocontrollerandroid.domain.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import kotlinx.coroutines.flow.Flow

interface GoProConnector {
    fun getNearByCameras(): Flow<GoProCamera>

    fun stopSearch(): Either<GoProError, Boolean>
    fun getCamerasPaired(): List<GoProCamera>
    suspend fun connectToDevice(address: String): Either<GoProError, Boolean>
    suspend fun getWifiApSSID(): Either<GoProError, String>
    suspend fun getWifiApPassword(): Either<GoProError, String>
    suspend fun enableWifiAp(): Either<GoProError, Boolean>
    suspend fun disableWifiAp(): Either<GoProError, Boolean>
    fun checkPermissions(): Boolean
    suspend fun setPresetsTimeLapse(): Either<GoProError, Boolean>
    suspend fun setPresetsPhoto(): Either<GoProError, Boolean>
    suspend fun setPresetsVideo(): Either<GoProError, Boolean>
    suspend fun getOpenGoProVersion(): Either<GoProError, String>
    suspend fun getCameraStatus(): Either<GoProError, Map<String, String>>
}