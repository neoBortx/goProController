package com.bortxapps.goprocontrollerandroid.feature.media

import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProMedia
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryBase
import com.bortxapps.goprocontrollerandroid.feature.media.api.MediaApi
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItem
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItems

class GoProMediaImpl(private val api: MediaApi = MediaApi()) : RepositoryBase(), GoProMedia {

    override suspend fun getMediaList() = launchRequest<MediaItems, MediaItems>(
        request = { api.getMediaList() }
    )

    override suspend fun getMediaInfo(fileName: String) = launchRequest<MediaItem, MediaItems>(
        request = { api.getMediaInfo(fileName = fileName) }
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
}