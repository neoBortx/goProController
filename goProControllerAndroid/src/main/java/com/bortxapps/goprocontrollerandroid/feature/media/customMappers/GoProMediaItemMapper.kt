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

internal fun goProMediaItemMapper(mediaItem: MediaItem, mediaDirectory: MediaDirectory, mediaInfo: MediaInfo) = GoProMediaItem(
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
    mediaHeight = mediaInfo.h?.toInt(),
    mediaWidth = mediaInfo.w?.toInt(),
    photoWithHDR = mediaInfo.hdr?.let { it == 1u },
    photoWithWDR = mediaInfo.wdr?.let { it == 1u },
    audioOption = mapAudioOption(mediaInfo.ao),
    videoFraneRateNumerator = mediaInfo.fps,
    videoFraneRateDenominator = mediaInfo.fpsDenom,
    videoDurationSeconds = mediaInfo.dur,
    videoWithImageStabilization = mediaInfo.eis?.let { it == 1u },
    groupImagesNames = mapGroupImages(mediaItem, mediaDirectory)
)

internal fun mapAudioOption(audioOption: AudioOption?) = when (audioOption) {
    AudioOption.OFF -> GoProAudioOption.OFF
    AudioOption.STEREO -> GoProAudioOption.STEREO
    AudioOption.AUTO -> GoProAudioOption.AUTO
    AudioOption.WIND -> GoProAudioOption.WIND
    else -> GoProAudioOption.UNKNOWN
}

internal fun mapMediaType(contentType: ContentType?): GoProMediaItemType = when (contentType) {
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

private const val FILENAME_SUFFIX_LENGTH = 3

internal fun mapGroupImages(mediaItem: MediaItem, mediaDirectory: MediaDirectory): List<GroupMediaItem> {
    return if (checkDataIsConsistent(mediaItem)) {
        val missingMembers = mediaItem.missingMemberOfGroup?.map { it.toInt() } ?: emptyList()
        val filenameType = mediaItem.fileName.substringAfterLast(".")
        val fileNamePrefix = mediaItem.fileName.substringBeforeLast(".").dropLast(FILENAME_SUFFIX_LENGTH)
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

internal fun checkDataIsConsistent(mediaItem: MediaItem) =
    listOf(mediaItem.firstMemberOfGroup, mediaItem.lastMemberOfGroup, mediaItem.groupId).all { it != null }
            && mediaItem.firstMemberOfGroup!!.toInt() <= mediaItem.lastMemberOfGroup!!.toInt()
            && mediaItem.fileName.contains(".")
            && mediaItem.fileName.substringBeforeLast(".").length >= FILENAME_SUFFIX_LENGTH