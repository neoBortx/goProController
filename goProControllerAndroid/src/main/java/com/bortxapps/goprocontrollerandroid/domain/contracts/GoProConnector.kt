package com.bortxapps.goprocontrollerandroid.domain.contracts

import com.bortxapps.goprocontrollerandroid.domain.data.GoProBleConnectionStatus
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import kotlinx.coroutines.flow.Flow

interface GoProConnector {
    suspend fun getNearByCameras():Result<Flow<GoProCamera>>
    suspend fun stopSearch(): Result<Boolean>
    suspend fun getCamerasPaired(): Result<List<GoProCamera>>
    suspend fun connectToDevice(address: String): Result<Boolean>
    suspend fun subscribeToBleConnectionStatusChanges(): Flow<GoProBleConnectionStatus>
    suspend fun disconnectBle(): Result<Boolean>
}