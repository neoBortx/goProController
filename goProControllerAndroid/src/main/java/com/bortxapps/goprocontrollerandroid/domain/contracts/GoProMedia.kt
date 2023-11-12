package com.bortxapps.goprocontrollerandroid.domain.contracts

import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItems
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiStatus
import kotlinx.coroutines.flow.Flow

interface GoProMedia {
    suspend fun getMediaList(): Result<MediaItems>
    suspend fun getMediaInfo(fileName: String): Result<MediaItems>
    suspend fun getMediaVideo(fileName: String): Result<ByteArray>
    suspend fun getMediaImage(fileName: String): Result<ByteArray>
    suspend fun getMediaThumbnail(fileName: String): Result<ByteArray>
    suspend fun getMediaScreenNail(fileName: String): Result<ByteArray>
    fun enableWifiInMobile(): Result<Boolean>
    fun connectToWifi(ssid: String, password: String): Flow<Result<WifiStatus>>
}