package com.bortxapps.goprocontrollerandroid.feature.media.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.GoProAudioOption
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItemType
import com.bortxapps.goprocontrollerandroid.feature.media.data.AudioOption
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaDirectory
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaInfo
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class GoProMediaItemMapperTest {

    @Test
    fun `goProMediaItemMapper should map MediaItem, MediaDirectory, and MediaInfo to GoProMediaItem`() {
        val mediaItem = MediaItem(
            fileName = "example.mp4",
            size = "1024",
            creationTimeStamp = 1636357038,
            mod = 1636358000
        )
        val mediaDirectory = MediaDirectory(
            directory = "videos",
            files = listOf(mediaItem)
        )
        val mediaInfo = MediaInfo(
            ct = com.bortxapps.goprocontrollerandroid.feature.media.data.ContentType.VIDEO,
            hdr = 1u,
            wdr = 1u,
            ao = AudioOption.STEREO,
            fps = 30u,
            fpsDenom = 1u,
            dur = 120u,
            eis = 1u,
            h = 1080u,
            w = 1920u
        )
        val result = goProMediaItemMapper(mediaItem, mediaDirectory, mediaInfo)

        assertEquals("", result.mediaId)
        assertEquals("example.mp4", result.fileName)
        assertEquals("videos", result.filePath)
        assertEquals("videos/example.mp4", result.fileFullUrl)
        assertEquals("http://10.5.5.9:8080/videos/DCIM/videos/example.mp4", result.fileMediaUrl)
        assertEquals(1024L, result.fileSize)
        assertEquals(1636357038L, result.creationTimeStamp)
        assertEquals(1636358000L, result.modTimeStamp)
        assertEquals("/gopro/media/thumbnail?path=videos/example.mp4", result.thumbnailUrl)
        assertEquals("/gopro/media/screennail?path=videos/example.mp4", result.screenNailUrl)
        assertEquals(GoProMediaItemType.VIDEO, result.mediaType)
        assertEquals(1080, result.mediaHeight)
        assertEquals(1920, result.mediaWidth)
        assertEquals(true, result.photoWithHDR)
        assertEquals(true, result.photoWithWDR)
        assertEquals(GoProAudioOption.STEREO, result.audioOption)
        assertEquals(30u, result.videoFraneRateNumerator)
        assertEquals(1u, result.videoFraneRateDenominator)
        assertEquals(120u, result.videoDurationSeconds)
        assertEquals(true, result.videoWithImageStabilization)
        assertTrue(result.groupImagesNames.isEmpty())
    }

    @Test
    fun `goProMediaItemMapper test null values`() {
        val mediaItem = MediaItem(
            fileName = "example.mp4",
        )
        val mediaDirectory = MediaDirectory(
            directory = "videos",
            files = listOf(mediaItem)
        )
        val mediaInfo = MediaInfo()
        val result = goProMediaItemMapper(mediaItem, mediaDirectory, mediaInfo)

        assertEquals("", result.mediaId)
        assertEquals("example.mp4", result.fileName)
        assertEquals("videos", result.filePath)
        assertEquals("videos/example.mp4", result.fileFullUrl)
        assertEquals("http://10.5.5.9:8080/videos/DCIM/videos/example.mp4", result.fileMediaUrl)
        assertNull(result.fileSize)
        assertNull(result.creationTimeStamp)
        assertNull(result.modTimeStamp)
        assertEquals("/gopro/media/thumbnail?path=videos/example.mp4", result.thumbnailUrl)
        assertEquals("/gopro/media/screennail?path=videos/example.mp4", result.screenNailUrl)
        assertEquals(GoProMediaItemType.UNKNOWN, result.mediaType)
        assertNull(result.mediaHeight)
        assertNull(result.mediaWidth)
        assertNull(result.photoWithHDR)
        assertNull(result.photoWithWDR)
        assertEquals(GoProAudioOption.UNKNOWN, result.audioOption)
        assertNull(result.videoFraneRateNumerator)
        assertNull(result.videoFraneRateDenominator)
        assertNull(result.videoDurationSeconds)
        assertNull(result.videoWithImageStabilization)
        assertTrue(result.groupImagesNames.isEmpty())
    }


    @Test
    fun `mapGroupImages should map MediaItem and MediaDirectory to List of GroupMediaItem`() {
        val mediaItem = MediaItem(
            fileName = "example_001.jpg",
            firstMemberOfGroup = "1",
            lastMemberOfGroup = "3",
            groupId = "group1",
            missingMemberOfGroup = listOf("2")
        )

        val mediaDirectory = MediaDirectory(
            directory = "photos",
            files = listOf(mediaItem)
        )
        val result = mapGroupImages(mediaItem, mediaDirectory)

        assertEquals(2, result.size)
        assertEquals("example_001.jpg", result[0].fileName)
        assertEquals("http://10.5.5.9:8080/videos/DCIM/photos/example_001.jpg", result[0].fileMediaUrl)
        assertEquals("example_003.jpg", result[1].fileName)
        assertEquals("http://10.5.5.9:8080/videos/DCIM/photos/example_003.jpg", result[1].fileMediaUrl)
    }

    @Test
    fun `mapGroupImages should return empty list when group information is missing`() {
        val mediaItem = MediaItem(
            fileName = "example.jpg"
        )

        val mediaDirectory = MediaDirectory(
            directory = "photos",
            files = listOf(mediaItem)
        )

        val result = mapGroupImages(mediaItem, mediaDirectory)

        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return empty list for empty MediaItem list`() {
        // Given
        val mediaDirectory = MediaDirectory(directory = "photos", files = emptyList())

        // When
        val result = mapGroupImages(MediaItem(fileName = "example.jpg"), mediaDirectory)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return empty list for MediaItem with missing group information`() {
        // Given
        val mediaDirectory = MediaDirectory(
            directory = "photos",
            files = listOf(MediaItem(fileName = "example.jpg"))
        )

        // When
        val result = mapGroupImages(MediaItem(fileName = "example.jpg"), mediaDirectory)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return empty list for MediaItem when firstMemberOfGroup is null`() {
        // Given
        val mediaItem = MediaItem(
            fileName = "example_001.jpg",
            firstMemberOfGroup = null,
            lastMemberOfGroup = "1",
            groupId = "group1"
        )

        val mediaDirectory = MediaDirectory(directory = "photos", files = listOf(mediaItem))

        // When
        val result = mapGroupImages(mediaItem, mediaDirectory)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return empty list for MediaItem when lastMemberOfGroup is null`() {
        // Given
        val mediaItem = MediaItem(
            fileName = "example_001.jpg",
            firstMemberOfGroup = "56",
            lastMemberOfGroup = null,
            groupId = "group1"
        )

        val mediaDirectory = MediaDirectory(directory = "photos", files = listOf(mediaItem))

        // When
        val result = mapGroupImages(mediaItem, mediaDirectory)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return empty list for MediaItem when fileName not have extension`() {
        // Given
        val mediaItem = MediaItem(
            fileName = "example_001",
            firstMemberOfGroup = "1",
            lastMemberOfGroup = "4",
            groupId = "group1"
        )

        val mediaDirectory = MediaDirectory(directory = "photos", files = listOf(mediaItem))

        // When
        val result = mapGroupImages(mediaItem, mediaDirectory)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return empty list for MediaItem when fileName is too short`() {
        // Given
        val mediaItem = MediaItem(
            fileName = "01",
            firstMemberOfGroup = "1",
            lastMemberOfGroup = "4",
            groupId = "group1"
        )

        val mediaDirectory = MediaDirectory(directory = "photos", files = listOf(mediaItem))

        // When
        val result = mapGroupImages(mediaItem, mediaDirectory)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return empty list for MediaItem when firstMemberOfGroup greater than lastMemberOfGroup`() {
        // Given
        val mediaItem = MediaItem(
            fileName = "example_001.jpg",
            firstMemberOfGroup = "3",
            lastMemberOfGroup = "1",
            groupId = "group1"
        )

        val mediaDirectory = MediaDirectory(directory = "photos", files = listOf(mediaItem))

        // When
        val result = mapGroupImages(mediaItem, mediaDirectory)

        // Then
        assertEquals(0, result.size)
    }

    @Test
    fun `mapGroupImages should return full list for MediaItem with missing members in group`() {
        // Given
        val mediaItem = MediaItem(
            fileName = "example_001.jpg",
            firstMemberOfGroup = "1",
            lastMemberOfGroup = "56",
            groupId = "group1",
            missingMemberOfGroup = null
        )

        val mediaDirectory = MediaDirectory(directory = "photos", files = listOf(mediaItem))

        // When
        val result = mapGroupImages(mediaItem, mediaDirectory)

        // Then
        assertEquals(56, result.size)
    }

    @Test
    fun `mapGroupImages should handle missing file name prefix`() {
        // Given
        val mediaItem = MediaItem(
            fileName = "001.jpg",
            firstMemberOfGroup = "1",
            lastMemberOfGroup = "3",
            groupId = "group1"
            // ... other properties
        )

        val mediaDirectory = MediaDirectory(directory = "photos", files = listOf(mediaItem))

        // When
        val result = mapGroupImages(mediaItem, mediaDirectory)

        // Then
        assertEquals(3, result.size)
        assertEquals("001.jpg", result[0].fileName)
        assertEquals("http://10.5.5.9:8080/videos/DCIM/photos/001.jpg", result[0].fileMediaUrl)
        assertEquals("002.jpg", result[1].fileName)
        assertEquals("http://10.5.5.9:8080/videos/DCIM/photos/002.jpg", result[1].fileMediaUrl)
        assertEquals("003.jpg", result[2].fileName)
        assertEquals("http://10.5.5.9:8080/videos/DCIM/photos/003.jpg", result[2].fileMediaUrl)
    }
}
