package com.bortxapps.goprocontrollerandroid.feature.media.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaDirectory(
    @SerialName("d")
    val directory: String,
    @SerialName("fs")
    val files: List<MediaItem>)