package com.bortxapps.goprocontrollerandroid.domain.data

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val BYTE_SIZE = 1024.0
private const val SIZE_DIVIDER = 3

data class GoProMediaItem(
    //Unique identifier of the media item
    val mediaId: String,
    //media item name
    val fileName: String,
    val filePath: String,
    val fileFullUrl: String,
    //Full URL to download the media item directly
    val fileMediaUrl: String,
    //Size of the media item in bytes
    val fileSize: Long?,
    //Creation timestamp of the media item
    val creationTimeStamp: Long?,
    //Last modification timestamp of the media item
    val modTimeStamp: Long?,
    //URL to access the thumbnail of the media item and get it as an input stream using the API
    val thumbnailUrl: String,
    //URL to access the screennail of the media item and get it as an input stream using the API
    val screenNailUrl: String,
    //Type of the media item
    val mediaType: GoProMediaItemType,
    //Height of the media item
    val mediaHeight: Int?,
    //Width of the media item
    val mediaWidth: Int?,

    //photo
    val photoWithHDR: Boolean? = null,
    val photoWithWDR: Boolean? = null,

    //video
    val audioOption: GoProAudioOption? = null,
    val videoFraneRateNumerator: UInt? = null,
    val videoFraneRateDenominator: UInt? = null,
    val videoDurationSeconds: UInt? = null,
    val videoWithImageStabilization: Boolean? = null,

    //groupImages
    val groupImagesNames: List<GroupMediaItem>
) {
    fun getSize(): String {
        val units = arrayOf("Bytes", "KB", "MB", "GB", "TB")

        if (fileSize == null || fileSize == 0L) {
            return "0 Byte"
        }

        val unitIndex = (Math.log10(fileSize.toDouble()) / SIZE_DIVIDER).toInt()
        val sizeConverted = fileSize / Math.pow(BYTE_SIZE, unitIndex.toDouble())

        return String.format(Locale.getDefault(), "%.2f %s", sizeConverted, units[unitIndex])
    }

    fun getCreationDate(): String {
        return creationTimeStamp?.let {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(creationTimeStamp))
        } ?: run {
            ""
        }
    }
}


