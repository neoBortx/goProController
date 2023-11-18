package com.bortxapps.goprocontrollerandroid.domain.data

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

data class GoProMediaItem(
    val mediaId: String,
    val fileName: String,
    val filePath: String,
    val fileFullUrl: String,
    val fileSize: Long?,
    val creationTimeStamp: Long?,
    val modTimeStamp: Long?,
    val thumbnailUrl: String,
    val screenNailUrl: String,
    val mediaType: GoProMediaItemType,
    val mediaHeight: Long,
    val mediaWidth: Long,

    //photo
    val photoWithHDR: Boolean? = null,
    val photoWithWDR: Boolean? = null,

    //video
    val audioOption: GoProAudioOption? = null,
    val videoFraneRateNumerator: UInt? = null,
    val videoFraneRateDenominator: UInt? = null,
    val videoDurationSeconds: UInt? = null,
    val videoWithImageStabilization: Boolean? = null,
) {
    fun getSize(): String {
        val units = arrayOf("Bytes", "KB", "MB", "GB", "TB")

        if (fileSize == null || fileSize == 0L) {
            return "0 Byte"
        }

        val unitIndex = (Math.log10(fileSize.toDouble()) / 3).toInt()
        val sizeConverted = fileSize / Math.pow(1024.0, unitIndex.toDouble())

        return String.format("%.2f %s", sizeConverted, units[unitIndex])
    }

    fun getCreationDate(): String {
        return creationTimeStamp?.let {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(creationTimeStamp))
        } ?: run {
            ""
        }
    }
}


