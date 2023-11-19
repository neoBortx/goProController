package com.bortxapps.goprocontrollerandroid.feature.media.api

import com.bortxapps.goprocontrollerandroid.feature.GET_MEDIA_INFO_URL
import com.bortxapps.goprocontrollerandroid.feature.GET_MEDIA_LIST
import com.bortxapps.goprocontrollerandroid.feature.GET_SCREENNAIL_URL
import com.bortxapps.goprocontrollerandroid.feature.GET_THUMBNAIL_URL
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.KtorClient
import io.ktor.http.ContentType

class MediaApi(private val client: KtorClient = KtorClient()) {
    suspend fun getMediaList() =
        client.getRawRequest(path = GET_MEDIA_LIST)

    suspend fun getMediaVideo(fileName: String) =
        client.getFile(
            path = fileName,
            contentType = ContentType.Video.MP4
        )

    suspend fun getMediaImage(fileName: String) =
        client.getFile(
            path = fileName,
            contentType = ContentType.Video.MPEG
        )

    suspend fun getMediaThumbnail(fileName: String) =
        client.getRawRequest(
            path = GET_THUMBNAIL_URL,
            queryParams = mapOf("path" to fileName)
        )

    suspend fun getMediaScreenNail(fileName: String) =
        client.getRawRequest(
            path = GET_SCREENNAIL_URL,
            queryParams = mapOf("path" to fileName)
        )

    suspend fun getMediaInfo(fileName: String) = client.getRawRequest(
        path = GET_MEDIA_INFO_URL,
        queryParams = mapOf("path" to fileName)
    )
}