package com.bortxapps.goprocontrollerandroid.domain.contracts

import com.bortxapps.goprocontrollerandroid.domain.data.FrameRate
import com.bortxapps.goprocontrollerandroid.domain.data.GoProSettings
import com.bortxapps.goprocontrollerandroid.domain.data.HyperSmooth
import com.bortxapps.goprocontrollerandroid.domain.data.Presets
import com.bortxapps.goprocontrollerandroid.domain.data.Resolution
import com.bortxapps.goprocontrollerandroid.domain.data.Speed
import kotlinx.coroutines.flow.Flow

interface GoProCommands {
    suspend fun subscribeToCameraSettingsChanges(): Flow<GoProSettings>
    suspend fun getWifiApSSID(): Result<String>
    suspend fun getWifiApPassword(): Result<String>
    suspend fun getOpenGoProVersion(): Result<String>
    suspend fun getCameraStatus(): Result<Map<String, String>>
    suspend fun getResolution(): Result<Resolution>
    suspend fun getFrameRate(): Result<FrameRate>
    suspend fun getHyperSmooth(): Result<HyperSmooth>
    suspend fun getSpeed(): Result<Speed>
    suspend fun getPresets(): Result<Presets>
    suspend fun enableWifiAp(): Result<Boolean>
    suspend fun disableWifiAp(): Result<Boolean>
    suspend fun setPresetsVideo(): Result<Boolean>
    suspend fun setPresetsPhoto(): Result<Boolean>
    suspend fun setPresetsTimeLapse(): Result<Boolean>
    suspend fun setResolution(resolution: Resolution): Result<Boolean>
    suspend fun setFrameRate(frameRate: FrameRate): Result<Boolean>
    suspend fun setHyperSmooth(hyperSmooth: HyperSmooth): Result<Boolean>
    suspend fun setSpeed(speed: Speed): Result<Boolean>
    suspend fun setShutterOn(): Result<Boolean>
    suspend fun setShutterOff(): Result<Boolean>
}