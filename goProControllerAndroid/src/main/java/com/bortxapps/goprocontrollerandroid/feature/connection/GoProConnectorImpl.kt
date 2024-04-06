package com.bortxapps.goprocontrollerandroid.feature.connection

import android.content.Context
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProConnector
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryBleBase
import com.bortxapps.goprocontrollerandroid.feature.base.mappers.mapBleException
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GOPRO_NAME_PREFIX
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.feature.connection.api.ConnectionApi
import com.bortxapps.goprocontrollerandroid.feature.connection.mapper.goProBleConnectionStateMapper
import com.bortxapps.goprocontrollerandroid.feature.connection.mapper.toMapCamera
import com.bortxapps.simplebleclient.exceptions.SimpleBleClientException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GoProConnectorImpl(
    context: Context,
    private val api: ConnectionApi
) : RepositoryBleBase(context), GoProConnector {

    override suspend fun getNearByCameras(): Result<Flow<GoProCamera>> {
        return Result.success(api.getNearByCameras(GoProUUID.SERVICE_UUID.uuid).map {
            it.toMapCamera()
        })
    }


    override suspend fun stopSearch(): Result<Boolean> {
        api.stopSearch()
        return Result.success(true)
    }

    override suspend fun getCamerasPaired(): Result<List<GoProCamera>> {
        return  try {
            Result.success(api.getPairedCameras(context, GOPRO_NAME_PREFIX).map {
                it.toMapCamera()
            })
        } catch (e : SimpleBleClientException) {
            Result.failure(mapBleException(e))
        }
    }

    override suspend fun connectToDevice(address: String): Result<Boolean> = try {
        if (api.connectToDevice(context, address)) {
            Result.success(true)
        } else {
            Result.failure(GoProException(GoProError.OTHER))
        }
    } catch (e: SimpleBleClientException) {
        Result.failure(mapBleException(e))
    } catch (e: Exception) {
        Log.e("BleManager", "connectToDevice ${e.message} ${e.stackTraceToString()}")
        Result.failure(GoProException(GoProError.OTHER))
    }

    override suspend fun disconnectBle(): Result<Boolean> {
        api.disconnectBle()
        return Result.success(true)
    }

    override suspend fun subscribeToBleConnectionStatusChanges() = api.subscribeToConnectionStatusChanges().map {
        goProBleConnectionStateMapper(it)
    }
}