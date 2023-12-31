package com.bortxapps.goprocontrollerandroid.domain.contracts

import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiStatus
import kotlinx.coroutines.flow.Flow

interface GoProMedia {
    suspend fun getMediaList(): Result<List<GoProMediaItem>>
    suspend fun getMediaThumbnail(fileName: String): Result<ByteArray>
    suspend fun getMediaScreenNail(fileName: String): Result<ByteArray>
    fun enableWifiInMobile(): Result<Boolean>
    fun connectToWifi(ssid: String, password: String): Flow<Result<WifiStatus>>
}