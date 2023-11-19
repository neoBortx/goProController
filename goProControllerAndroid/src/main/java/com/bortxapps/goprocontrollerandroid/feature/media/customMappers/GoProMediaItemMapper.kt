package com.bortxapps.goprocontrollerandroid.feature.media.customMappers

import com.bortxapps.goprocontrollerandroid.domain.data.GoProAudioOption
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItem
import com.bortxapps.goprocontrollerandroid.domain.data.GoProMediaItemType
import com.bortxapps.goprocontrollerandroid.domain.data.GroupMediaItem
import com.bortxapps.goprocontrollerandroid.feature.GET_SCREENNAIL_URL
import com.bortxapps.goprocontrollerandroid.feature.GET_THUMBNAIL_URL
import com.bortxapps.goprocontrollerandroid.feature.GOPRO_MEDIA_PATH
import com.bortxapps.goprocontrollerandroid.feature.media.data.AudioOption
import com.bortxapps.goprocontrollerandroid.feature.media.data.ContentType
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaDirectory
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaInfo
import com.bortxapps.goprocontrollerandroid.feature.media.data.MediaItem
import java.util.Locale

fun goProMediaItemMapper(mediaItem: MediaItem, mediaDirectory: MediaDirectory, mediaInfo: MediaInfo) = GoProMediaItem(
    mediaId = "",
    fileName = mediaItem.fileName,
    filePath = mediaDirectory.directory,
    fileFullUrl = mediaDirectory.directory + "/" + mediaItem.fileName,
    fileMediaUrl = GOPRO_MEDIA_PATH + mediaDirectory.directory + "/" + mediaItem.fileName,
    fileSize = mediaItem.size?.toLong(),
    creationTimeStamp = mediaItem.creationTimeStamp,
    modTimeStamp = mediaItem.mod,
    thumbnailUrl = GET_THUMBNAIL_URL + "?path=" + mediaDirectory.directory + "/" + mediaItem.fileName,
    screenNailUrl = GET_SCREENNAIL_URL + "?path=" + mediaDirectory.directory + "/" + mediaItem.fileName,
    mediaType = mapMediaType(mediaInfo.ct),
    mediaHeight = 0,
    mediaWidth = 0,
    photoWithHDR = mediaInfo.hdr == 1u,
    photoWithWDR = mediaInfo.wdr == 1u,
    audioOption = mapAudioOption(mediaInfo.ao),
    videoFraneRateNumerator = mediaInfo.fps,
    videoFraneRateDenominator = mediaInfo.fpsDenom,
    videoDurationSeconds = mediaInfo.dur,
    videoWithImageStabilization = mediaInfo.eis == 1u,
    groupImagesNames = mapGroupImages(mediaItem, mediaDirectory)
)

fun mapAudioOption(audioOption: AudioOption?) = when (audioOption) {
    AudioOption.OFF -> GoProAudioOption.OFF
    AudioOption.STEREO -> GoProAudioOption.STEREO
    AudioOption.AUTO -> GoProAudioOption.AUTO
    AudioOption.WIND -> GoProAudioOption.WIND
    else -> GoProAudioOption.UNKNOWN
}

fun mapMediaType(contentType: ContentType?): GoProMediaItemType = when (contentType) {
    ContentType.VIDEO -> GoProMediaItemType.VIDEO
    ContentType.LOOPING -> GoProMediaItemType.LOOPING
    ContentType.CHAPTERED_VIDEO -> GoProMediaItemType.CHAPTERED_VIDEO
    ContentType.TIME_LAPSE -> GoProMediaItemType.TIMELAPSE
    ContentType.SINGLE_PHOTO -> GoProMediaItemType.SINGLE_PHOTO
    ContentType.BURST_PHOTO -> GoProMediaItemType.BURST_PHOTO
    ContentType.TIME_LAPSE_PHOTO -> GoProMediaItemType.TIMELAPSE_PHOTO
    ContentType.NIGHT_LAPSE_PHOTO -> GoProMediaItemType.NIGHT_LAPSE_PHOTO
    ContentType.NIGHT_PHOTO -> GoProMediaItemType.NIGHT_PHOTO
    ContentType.CONTINUOUS_PHOTO -> GoProMediaItemType.CONTINUOUS_PHOTO
    ContentType.RAW_PHOTO -> GoProMediaItemType.RAW_PHOTO
    ContentType.LIVE_BURST -> GoProMediaItemType.LIVE_BURST
    else -> GoProMediaItemType.UNKNOWN
}

fun mapGroupImages(mediaItem: MediaItem, mediaDirectory: MediaDirectory): List<GroupMediaItem> {
    return if (listOf(mediaItem.firstMemberOfGroup, mediaItem.lastMemberOfGroup, mediaItem.groupId).all { it != null }) {
        val prefixLength = 3
        val missingMembers = mediaItem.missingMemberOfGroup?.map { it.toInt() } ?: emptyList()
        val filenameType = mediaItem.fileName.substringAfterLast(".")
        val fileNamePrefix = mediaItem.fileName.substringBeforeLast(".").dropLast(prefixLength)

        (mediaItem.firstMemberOfGroup!!.toInt()..mediaItem.lastMemberOfGroup!!.toInt())
            .subtract(missingMembers.toSet())
            .map {
                val fileName = fileNamePrefix + String.format(Locale.getDefault(), "%03d", it) + "." + filenameType
                GroupMediaItem(fileName, GOPRO_MEDIA_PATH + mediaDirectory.directory + "/" + fileName)
            }
    } else {
        emptyList()
    }
}