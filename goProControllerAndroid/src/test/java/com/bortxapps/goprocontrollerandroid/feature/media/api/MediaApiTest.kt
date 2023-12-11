package com.bortxapps.goprocontrollerandroid.feature.media.api

import com.bortxapps.goprocontrollerandroid.feature.GET_MEDIA_INFO_URL
import com.bortxapps.goprocontrollerandroid.feature.GET_MEDIA_LIST
import com.bortxapps.goprocontrollerandroid.feature.GET_SCREENNAIL_URL
import com.bortxapps.goprocontrollerandroid.feature.GET_THUMBNAIL_URL
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.KtorClient
import io.ktor.http.ContentType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.unmockkAll
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test

class MediaApiTest {

    private lateinit var mediaApi: MediaApi
    private lateinit var ktorClient: KtorClient

    @Before
    fun setUp() {
        ktorClient = mockk(relaxed = true)
        mediaApi = MediaApi(ktorClient)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `getMediaList should call client's getRawRequest with correct path`() = runBlocking {
        // Given
        coEvery { ktorClient.getRawRequest(path = any()) } returns mockk()

        // When
        mediaApi.getMediaList()

        // Then
        coVerify { ktorClient.getRawRequest(path = GET_MEDIA_LIST) }
    }

    @Test
    fun `getMediaVideo should call client's getFile with correct path and content type`() = runBlocking {
        // Given
        val fileName = "video.mp4"
        coEvery { ktorClient.getFile(path = any(), contentType = any()) } returns mockk()

        // When
        mediaApi.getMediaVideo(fileName)

        // Then
        coVerify { ktorClient.getFile(path = fileName, contentType = ContentType.Video.MP4) }
    }

    @Test
    fun `getMediaImage should call client's getFile with correct path and content type`() = runBlocking {
        // Given
        val fileName = "image.jpg"
        coEvery { ktorClient.getFile(path = any(), contentType = any()) } returns mockk()

        // When
        mediaApi.getMediaImage(fileName)

        // Then
        coVerify { ktorClient.getFile(path = fileName, contentType = ContentType.Video.MPEG) }
    }

    @Test
    fun `getMediaThumbnail should call client's getRawRequest with correct path and queryParams`() = runBlocking {
        // Given
        val fileName = "thumbnail.jpg"
        coEvery { ktorClient.getRawRequest(path = any(), queryParams = any()) } returns mockk()

        // When
        mediaApi.getMediaThumbnail(fileName)

        // Then
        coVerify { ktorClient.getRawRequest(path = GET_THUMBNAIL_URL, queryParams = mapOf("path" to fileName)) }
    }

    @Test
    fun `getMediaScreenNail should call client's getRawRequest with correct path and queryParams`() = runBlocking {
        // Given
        val fileName = "screennail.jpg"
        coEvery { ktorClient.getRawRequest(path = any(), queryParams = any()) } returns mockk()

        // When
        mediaApi.getMediaScreenNail(fileName)

        // Then
        coVerify { ktorClient.getRawRequest(path = GET_SCREENNAIL_URL, queryParams = mapOf("path" to fileName)) }
    }

    @Test
    fun `getMediaInfo should call client's getRawRequest with correct path and queryParams`() = runBlocking {
        // Given
        val fileName = "media_info.json"
        coEvery { ktorClient.getRawRequest(path = any(), queryParams = any()) } returns mockk()

        // When
        mediaApi.getMediaInfo(fileName)

        // Then
        coVerify { ktorClient.getRawRequest(path = GET_MEDIA_INFO_URL, queryParams = mapOf("path" to fileName)) }
    }
}
