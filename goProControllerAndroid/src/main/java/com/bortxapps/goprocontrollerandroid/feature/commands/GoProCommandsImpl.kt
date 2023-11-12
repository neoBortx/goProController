package com.bortxapps.goprocontrollerandroid.feature.commands

import android.content.Context
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProCommands
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryBleBase
import com.bortxapps.goprocontrollerandroid.feature.commands.api.CommandsApi
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapFrameRate
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapHyperSmooth
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapResolution
import com.bortxapps.goprocontrollerandroid.feature.commands.data.CameraStatus
import com.bortxapps.goprocontrollerandroid.feature.connection.decoder.decodeMessageAsMap
import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapPresets
import com.bortxapps.goprocontrollerandroid.feature.commands.customMappers.mapSpeed

class GoProCommandsImpl(
    context: Context,
    private val api: CommandsApi = CommandsApi()
) : RepositoryBleBase(context), GoProCommands {
    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getWifiApSSID() = launchReadRequest(
        request = { api.getWifiApSSID() },
        customMapper = { String(it.data.toByteArray(), Charsets.UTF_8) }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getWifiApPassword() = launchReadRequest(
        request = { api.getWifiApPassword() },
        customMapper = { String(it.data.toByteArray(), Charsets.UTF_8) }
    )

    override suspend fun enableWifiAp() = launchSimpleWriteRequest(
        request = { api.enableWifiAp() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun disableWifiAp() = launchSimpleWriteRequest(
        request = { api.disableWifiAp() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getOpenGoProVersion() = launchComplexWriteRequest(
        request = { api.getOpenGoProVersion() },
        customMapper = { "${it.data[0].toInt()}.${it.data[1].toInt()}" }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getCameraStatus() = launchComplexWriteRequest(
        request = { api.getCameraStatus() },
        customMapper = { CameraStatus.decodeStatus(decodeMessageAsMap(it)) }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getResolution() = launchComplexWriteRequest(
        request = { api.getResolution() },
        customMapper = { mapResolution(it.data.last()) }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getFrameRate() = launchComplexWriteRequest(
        request = { api.getFrameRate() },
        customMapper = { mapFrameRate(it.data.last()) }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getHyperSmooth() = launchComplexWriteRequest(
        request = { api.getHyperSmooth() },
        customMapper = { mapHyperSmooth(it.data.last()) }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getSpeed() = launchComplexWriteRequest(
        request = { api.getSpeed() },
        customMapper = { mapSpeed(it.data.last()) }
    )

    @OptIn(ExperimentalUnsignedTypes::class)
    override suspend fun getPresets() = launchComplexWriteRequest(
        request = { api.getPresets() },
        customMapper = { mapPresets(it.data.last()) }
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

    override suspend fun setShutterOff() = launchSimpleWriteRequest(
        request = { api.setShutterOff() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setShutterOn() = launchSimpleWriteRequest(
        request = { api.setShutterOn() },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setResolution(resolution: Resolution) = launchSimpleWriteRequest(
        request = { api.setResolution(resolution) },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setFrameRate(frameRate: FrameRate) = launchSimpleWriteRequest(
        request = { api.setFrameRate(frameRate) },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setHyperSmooth(hyperSmooth: HyperSmooth) = launchSimpleWriteRequest(
        request = { api.setHyperSmooth(hyperSmooth) },
        responseValidator = { validateSimpleWriteResponse(it) }
    )

    override suspend fun setSpeed(speed: Speed) = launchSimpleWriteRequest(
        request = { api.setSpeed(speed) },
        responseValidator = { validateSimpleWriteResponse(it) }
    )
}