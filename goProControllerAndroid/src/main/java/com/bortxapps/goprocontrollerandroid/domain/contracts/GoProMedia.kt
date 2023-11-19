package com.bortxapps.goprocontrollerandroid.domain.contracts

import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItems
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiStatus
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface GoProMedia {
    suspend fun getMediaList(): Result<List<GoProMediaItem>>
    suspend fun getMediaInfo(filePath: String): Result<MediaItems>
    suspend fun getMediaVideo(fileName: String): Result<InputStream>
    suspend fun getMediaImage(fileName: String): Result<InputStream>
    suspend fun getMediaThumbnail(fileName: String): Result<ByteArray>
    suspend fun getMediaScreenNail(fileName: String): Result<ByteArray>
    fun enableWifiInMobile(): Result<Boolean>
    fun connectToWifi(ssid: String, password: String): Flow<Result<WifiStatus>>
}