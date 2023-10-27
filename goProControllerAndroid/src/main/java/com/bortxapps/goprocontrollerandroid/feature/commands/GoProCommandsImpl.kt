package com.bortxapps.goprocontrollerandroid.feature.commands

import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProCommands
import com.bortxapps.goprocontrollerandroid.feature.QUERY_PARAM_SETTING_FRAME_RATE
import com.bortxapps.goprocontrollerandroid.feature.QUER_PARAM_SETTINGS_RESOLUTION
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryBase
import com.bortxapps.goprocontrollerandroid.feature.commands.api.CommandsApi
import com.bortxapps.goprocontrollerandroid.feature.commands.customSerializers.customCameraStateSerializer
import java.util.Date

class GoProCommandsImpl(private val api: CommandsApi = CommandsApi()) : RepositoryBase(), GoProCommands {
    override suspend fun getCameraState() = launchRequest<Map<String, String>, Map<String, String>>(
        customSerializer = { customCameraStateSerializer(it) },
        request = { api.getCameraState() }
    )

    override suspend fun setDigitalZoom(zoom: Int) = launchRequest<String, String>(request = { api.setDigitalZoom(zoom) })
    override suspend fun getDateTime() = launchRequest<String, String>(request = { api.getDateTime() })
    override suspend fun setDateTime(date: Date) = launchRequest<String, String>(request = { api.setDateTime(date) })
    override suspend fun getKeepAlive() = launchRequest<String, String>(request = { api.getKeepAlive() })
    override suspend fun startCameraStream() = launchRequest<String, String>(request = { api.startCameraStream() })
    override suspend fun stopCameraStream() = launchRequest<String, String>(request = { api.stopCameraStream() })
    override suspend fun startShutterStream() = launchRequest<String, String>(request = { api.startShutterStream() })
    override suspend fun stopShutterStream() = launchRequest<String, String>(request = { api.stopShutterStream() })
    override suspend fun getCameraPresets() = launchRequest<String, String>(request = { api.getCameraPresets() })
    override suspend fun setCameraVideoPresets() = launchRequest<String, String>(request = { api.setCameraVideoPresets() })
    override suspend fun setCameraPhotoPresets() = launchRequest<String, String>(request = { api.setCameraPhotoPresets() })
    override suspend fun setCameraTimeLapsePresets() = launchRequest<String, String>(request = { api.setCameraTimeLapsePresets() })
    override suspend fun setFrameRate(frameRate: QUERY_PARAM_SETTING_FRAME_RATE) = launchRequest<String, String>(request = { api.setFrameRate(frameRate) })
    override suspend fun setResolution(res: QUER_PARAM_SETTINGS_RESOLUTION) = launchRequest<String, String>(request = { api.setResolution(res) })

