package com.bortxapps.goprocontrollerandroid.feature.connection

import android.annotation.SuppressLint
import android.content.Context
import arrow.core.Either
import com.bortxapps.goprocontrollerandroid.domain.GoProError
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProConnector
import com.bortxapps.goprocontrollerandroid.domain.data.GoProCamera
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryBaseBle
import com.bortxapps.goprocontrollerandroid.feature.commands.data.CameraStatus
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GOPRO_NAME_PREFIX
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.goprocontrollerandroid.feature.connection.api.ConnectionApi
import com.bortxapps.goprocontrollerandroid.feature.connection.decoder.decodeMessageAsMap
import com.bortxapps.goprocontrollerandroid.feature.connection.mapper.toMapCamera
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.nio.charset.Charset

@OptIn(ExperimentalUnsignedTypes::class)
class GoProConnectorImpl(private val context: Context, private val api: ConnectionApi = ConnectionApi()) : RepositoryBaseBle(), GoProConnector {

    override fun checkPermissions() = api.checkPermissions(context)

    @SuppressLint("MissingPermission")
    override fun getNearByCameras(): Flow<GoProCamera> = api.getNearByCameras(context, GoProUUID.SERVICE_UUID.uuid).map {
        it.toMapCamera()
    }

    override fun stopSearch(): Either<GoProError, Boolean> = try {
        api.stopSearch(context)
        Either.Right(true)
    } catch (e: Exception) {
        e.printStackTrace()
        Either.Left(GoProError.OTHER)
    }

    override fun getCamerasPaired() = api.getPairedCameras(context, GOPRO_NAME_PREFIX).map {
        it.toMapCamera()
    }

    override suspend fun connectToDevice(address: String): Either<GoProError, Boolean> {
        if (api.connectToDevice(context, address)) {
            getWifiApSSID().onRight {
                return Either.Right(true)
            }.onLeft {
                return Either.Left(GoProError.OTHER)
            }
        } else {
            return Either.Left(GoProError.OTHER)
        }

        return Either.Left(GoProError.OTHER)
    }

    override suspend fun getWifiApSSID() = launchReadRequest(
        request = { api.getWifiApSSID() },
        customMapper = { String(it.data.toByteArray(), Charsets.UTF_8) }
    )

    override suspend fun getWifiApPassword() = launchReadRequest(
        request = { api.getWifiApPassword() },
        customMapper = { it.data.contentToString() }
    )

    override suspend fun enableWifiAp() = launchSimpleWriteRequest(
        request = { api.enableWifiAp() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun disableWifiAp() = launchSimpleWriteRequest(
        request = { api.disableWifiAp() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setPresetsVideo() = launchSimpleWriteRequest(
        request = { api.setPresetsVideo() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setPresetsPhoto() = launchSimpleWriteRequest(
        request = { api.setPresetsPhoto() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setPresetsTimeLapse() = launchSimpleWriteRequest(
        request = { api.setPresetsTimeLapse() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun getOpenGoProVersion() = launchComplexWriteRequest(
        request = { api.getOpenGoProVersion() },
        customMapper = { "${it.data[0].toInt()}.${it.data[1].toInt()}" }
    )
    override suspend fun getCameraStatus() = launchComplexWriteRequest(
        request = { api.getCameraStatus() },
        customMapper = { CameraStatus.decodeStatus(decodeMessageAsMap(it)) }
    )

    private fun validateSimpleWriteResponse(response: BleNetworkMessage) = response.data.last() == 0.toUByte()
}