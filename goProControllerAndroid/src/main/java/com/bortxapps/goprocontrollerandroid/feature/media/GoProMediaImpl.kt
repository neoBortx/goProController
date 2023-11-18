package com.bortxapps.goprocontrollerandroid.feature.media

import android.content.Context
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProMedia
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryBase
import com.bortxapps.goprocontrollerandroid.feature.media.api.MediaApi
import com.bortxapps.goprocontrollerandroid.feature.media.customMappers.goProMediaItemMapper
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaInfo
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItem
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItems
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiManager
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiStatus
import kotlinx.coroutines.flow.Flow

class GoProMediaImpl(
    private val context: Context,
    private val api: MediaApi = MediaApi(),
    private val wifiManager: WifiManager = WifiManager()
) : RepositoryBase(), GoProMedia {

    override suspend fun getMediaList() = try {
        val mediaItems = launchSimpleRequest<MediaItems>(request = { api.getMediaList() })

        Result.success(mediaItems.media.map { directory ->
            directory.files.map { mediaItem ->
                val mediaInfo = launchSimpleRequest<MediaInfo>(request = { api.getMediaInfo(directory.directory + "/" + mediaItem.fileName) })
                goProMediaItemMapper(mediaItem, directory, mediaInfo)
            }
        }.flatten())

    } catch (e: Exception) {
        Log.e("GoProMediaImpl", "Error getting media list ${e.message} -> ${e.stackTraceToString()}")
        Result.failure(GoProException(GoProError.CAMERA_API_ERROR))
    }


    override suspend fun getMediaInfo(filePath: String) = launchRequest<MediaItem, MediaItems>(
        request = { api.getMediaInfo(fileName = filePath) }
    )

    override suspend fun getMediaVideo(fileName: String) = launchRequest<ByteArray, ByteArray>(
        request = { api.getMediaVideo(fileName = fileName) }
    )

    override suspend fun getMediaImage(fileName: String) = launchRequest<ByteArray, ByteArray>(
        request = { api.getMediaImage(fileName = fileName) }
    )

    override suspend fun getMediaThumbnail(fileName: String) = launchRequest<ByteArray, ByteArray>(
        request = { api.getMediaThumbnail(fileName = fileName) }
    )

    override suspend fun getMediaScreenNail(fileName: String) = launchRequest<ByteArray, ByteArray>(
        request = { api.getMediaScreenNail(fileName) }
    )

    override fun enableWifiInMobile(): Result<Boolean> = try {
        wifiManager.enableWifi(context)
        Result.success(true)
    } catch (e: Exception) {
        Log.e("GoProMediaImpl", "Error enabling wifi", e)
        Result.failure(GoProException(GoProError.UNABLE_TO_ENABLE_WIFI))
    }

    override fun connectToWifi(
        ssid: String,
        password: String
    ): Flow<Result<WifiStatus>> = try {
        wifiManager.connectToWifi(ssid, password, context)
    } catch (e: Exception) {
        throw GoProException(GoProError.OTHER)
    }
}