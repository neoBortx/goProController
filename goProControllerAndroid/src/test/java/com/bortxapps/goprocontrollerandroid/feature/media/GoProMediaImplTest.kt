package com.bortxapps.goprocontrollerandroid.feature.media

import android.content.Context
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItemType
import com.bortxapps.goprocontrollerandroid.feature.media.api.MediaApi
import com.bortxapps.goprocontrollerandroid.feature.media.customMappers.goProMediaItemMapper
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaDirectory
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaInfo
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItem
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItems
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiManager
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GoProMediaImplTest {

    private val mockedContext = mockk<Context>(relaxed = true)
    private val mediaApi = mockk<MediaApi>(relaxed = true)
    private val wifiManager = mockk<WifiManager>(relaxed = true)
    private val httpResponseMock = mockk<HttpResponse>(relaxed = true)
    private val goProMedia = GoProMediaImpl(mockedContext, mediaApi, wifiManager)

    @Before
    fun setUp() {
        mockkStatic(::goProMediaItemMapper)
    }


    @Test
    fun `getMediaList throws exception expect CAMERA_API_ERROR`() = runBlocking {
        // Given
        coEvery { mediaApi.getMediaList() } throws Exception()

        // When
        val result = goProMedia.getMediaList()

        // Then
        coVerify { mediaApi.getMediaList() }
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as GoProException).goProError)
    }

    @Test
    fun `getMediaList throws GoProException expect throw Exception`() = runBlocking {
        // Given
        coEvery { mediaApi.getMediaList() } throws GoProException(goProError = GoProError.UNABLE_TO_MAP_DATA)

        // When
        val result = goProMedia.getMediaList()

        // Then
        coVerify { mediaApi.getMediaList() }
        assertTrue(result.isFailure)
        assertEquals(GoProError.UNABLE_TO_MAP_DATA, (result.exceptionOrNull() as GoProException).goProError)
    }

    @Test
    fun `getMediaList returns ERROR SHOULD CAMERA_API_ERROR`() = runBlocking {
        // Given
        coEvery { mediaApi.getMediaList() } throws Exception()

        // When
        val result = goProMedia.getMediaList()

        // Then
        coVerify { mediaApi.getMediaList() }
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as GoProException).goProError)
    }

    @Test
    fun `getMediaList returns Error code expect CAMERA_API_ERROR`() = runBlocking {
        // Given
        coEvery { httpResponseMock.status } returns HttpStatusCode.BadRequest
        coEvery { mediaApi.getMediaList() } returns httpResponseMock

        // When
        val result = goProMedia.getMediaList()

        // Then
        coVerify { mediaApi.getMediaList() }
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as GoProException).goProError)
    }

    @Test
    fun `getMediaList returns empty folder list expect empty list`() = runBlocking {
        // Given
        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<MediaItems>() } returns MediaItems("", emptyList())
        coEvery { mediaApi.getMediaList() } returns httpResponseMock

        // When
        val result = goProMedia.getMediaList()

        // Then
        coVerify { mediaApi.getMediaList() }
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty()!!)
    }

    @Test
    fun `getMediaList returns empty file list expect empty list`() = runBlocking {
        // Given
        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<MediaItems>() } returns MediaItems("", listOf(MediaDirectory("", emptyList())))
        coEvery { mediaApi.getMediaList() } returns httpResponseMock

        // When
        val result = goProMedia.getMediaList()

        // Then
        coVerify { mediaApi.getMediaList() }
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty()!!)
    }

    @Test
    fun `getMediaList invokes getMediaInfo and throws Exception expect Exception`() = runBlocking {
        // Given
        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<MediaItems>() } returns MediaItems(
            "", listOf(
                MediaDirectory(
                    "d",
                    listOf(MediaItem("fileName1"))
                )
            )
        )
        coEvery { mediaApi.getMediaList() } returns httpResponseMock
        coEvery { mediaApi.getMediaInfo("d/fileName1") } throws Exception()

        // When
        val result = goProMedia.getMediaList()

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as GoProException).goProError)
    }

    @Test
    fun `getMediaList invokes getMediaInfo and throws GoProException expect Exception`() = runBlocking {
        // Given
        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<MediaItems>() } returns MediaItems(
            "", listOf(
                MediaDirectory(
                    "d",
                    listOf(MediaItem("fileName1"))
                )
            )
        )
        coEvery { mediaApi.getMediaList() } returns httpResponseMock
        coEvery { mediaApi.getMediaInfo("d/fileName1") } throws GoProException(GoProError.UNABLE_TO_MAP_DATA)

        // When
        val result = goProMedia.getMediaList()

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.UNABLE_TO_MAP_DATA, (result.exceptionOrNull() as GoProException).goProError)
    }

    @Test
    fun `getMediaList getMediaInfo success goProMediaItemMapper throws Exception expect Exception`() = runBlocking {
        // Given
        val httpResponseMockMediaInfo = mockk<HttpResponse>(relaxed = true)

        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMockMediaInfo.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<MediaItems>() } returns MediaItems(
            "", listOf(
                MediaDirectory(
                    "d",
                    listOf(MediaItem("fileName1"))
                )
            )
        )
        coEvery { httpResponseMockMediaInfo.body<MediaInfo>() } returns MediaInfo()
        coEvery { mediaApi.getMediaList() } returns httpResponseMock
        coEvery { mediaApi.getMediaInfo("d/fileName1") } returns httpResponseMockMediaInfo
        coEvery { goProMediaItemMapper(any(), any(), any()) } throws Exception()

        // When
        val result = goProMedia.getMediaList()

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as GoProException).goProError)
    }

    @Test
    fun `getMediaList getMediaInfo goProMediaItemMapper success  expect success expect DTO success`() = runBlocking {
        // Given
        val httpResponseMockMediaInfo = mockk<HttpResponse>(relaxed = true)

        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMockMediaInfo.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<MediaItems>() } returns MediaItems(
            "", listOf(
                MediaDirectory(
                    "d",
                    listOf(MediaItem("fileName1"))
                )
            )
        )
        coEvery { httpResponseMockMediaInfo.body<MediaInfo>() } returns MediaInfo()
        coEvery { mediaApi.getMediaList() } returns httpResponseMock
        coEvery { mediaApi.getMediaInfo("d/fileName1") } returns httpResponseMockMediaInfo
        coEvery { goProMediaItemMapper(any(), any(), any()) } returns GoProMediaItem(
            mediaId = "varius",
            fileName = "Leta Collier",
            filePath = "primis",
            fileFullUrl = "http://www.bing.com/search?q=possit",
            fileMediaUrl = "https://duckduckgo.com/?q=potenti",
            fileSize = null,
            creationTimeStamp = null,
            modTimeStamp = null,
            thumbnailUrl = "https://duckduckgo.com/?q=expetenda",
            screenNailUrl = "http://www.bing.com/search?q=adipiscing",
            mediaType =  GoProMediaItemType.LOOPING,
            mediaHeight = null,
            mediaWidth = null,
            photoWithHDR = null,
            photoWithWDR = null,
            audioOption = null,
            videoFraneRateNumerator = null,
            videoFraneRateDenominator = null,
            videoDurationSeconds = null,
            videoWithImageStabilization = null,
            groupImagesNames = listOf()
        )

        // When
        val result = goProMedia.getMediaList()

        // Then
        assertTrue(result.isSuccess)
    }

    @Test
    fun `getMediaThumbnail success expect Success result`() = runBlocking {
        // Given
        val fileName = "fileName1"
        val data = ByteArray(10)
        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<ByteArray>() } returns data
        coEvery { mediaApi.getMediaThumbnail(fileName) } returns httpResponseMock


        // When
        val result = goProMedia.getMediaThumbnail(fileName)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(data, result.getOrNull())
    }

    @Test
    fun `getMediaThumbnail throws Exception expect GoProException`() = runBlocking {
        // Given
        val fileName = "fileName1"
        coEvery { mediaApi.getMediaThumbnail(fileName) } throws Exception()

        // When
        val result = goProMedia.getMediaThumbnail(fileName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as  GoProException).goProError)
    }

    @Test
    fun `getMediaThumbnail returns error expect GoProException`() = runBlocking {
        // Given
        val fileName = "fileName1"
        val data = ByteArray(10)
        coEvery { httpResponseMock.status } returns HttpStatusCode.BadRequest
        coEvery { httpResponseMock.body<ByteArray>() } returns data
        coEvery { mediaApi.getMediaThumbnail(fileName) } returns httpResponseMock


        // When
        val result = goProMedia.getMediaThumbnail(fileName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as  GoProException).goProError)
    }

    @Test
    fun `getMediaThumbnail throws GoProError expect GoProException`() = runBlocking {
        // Given
        val fileName = "fileName1"
        coEvery { mediaApi.getMediaThumbnail(fileName) } throws GoProException(GoProError.OTHER)

        // When
        val result = goProMedia.getMediaThumbnail(fileName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.OTHER, (result.exceptionOrNull() as  GoProException).goProError)
    }

    @Test
    fun `getMediaThumbnail Unable To serialize expect GoProException`() = runBlocking {
        // Given
        val fileName = "fileName1"
        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<MediaItems>() } returns MediaItems("", emptyList())
        coEvery { mediaApi.getMediaThumbnail(fileName) } returns httpResponseMock


        // When
        val result = goProMedia.getMediaThumbnail(fileName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.UNKNOWN_RECEIVED_DATA, (result.exceptionOrNull() as  GoProException).goProError)
    }


    @Test
    fun `getMediaScreenNail success expect Success result`() = runBlocking {
        // Given
        val fileName = "fileName1"
        val data = ByteArray(10)
        coEvery { httpResponseMock.status } returns HttpStatusCode.OK
        coEvery { httpResponseMock.body<ByteArray>() } returns data
        coEvery { mediaApi.getMediaScreenNail(fileName) } returns httpResponseMock


        // When
        val result = goProMedia.getMediaScreenNail(fileName)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(data, result.getOrNull())
    }

    @Test
    fun `getMediaScreenNail throws Exception expect GoProException`() = runBlocking {
        // Given
        val fileName = "fileName1"
        coEvery { mediaApi.getMediaScreenNail(fileName) } throws Exception()

        // When
        val result = goProMedia.getMediaScreenNail(fileName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as  GoProException).goProError)
    }

    @Test
    fun `getMediaScreenNail throws GoProError expect GoProException`() = runBlocking {
        // Given
        val fileName = "fileName1"
        coEvery { mediaApi.getMediaScreenNail(fileName) } throws GoProException(GoProError.OTHER)

        // When
        val result = goProMedia.getMediaScreenNail(fileName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.OTHER, (result.exceptionOrNull() as  GoProException).goProError)
    }

    @Test
    fun `getMediaScreenNail returns error expect GoProException`() = runBlocking {
        // Given
        val fileName = "fileName1"
        val data = ByteArray(10)
        coEvery { httpResponseMock.status } returns HttpStatusCode.BadRequest
        coEvery { httpResponseMock.body<ByteArray>() } returns data
        coEvery { mediaApi.getMediaScreenNail(fileName) } returns httpResponseMock

        // When
        val result = goProMedia.getMediaScreenNail(fileName)

        // Then
        assertTrue(result.isFailure)
        assertEquals(GoProError.CAMERA_API_ERROR, (result.exceptionOrNull() as  GoProException).goProError)
    }


}