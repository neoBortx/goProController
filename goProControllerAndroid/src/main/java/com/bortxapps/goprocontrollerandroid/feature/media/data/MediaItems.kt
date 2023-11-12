package com.bortxapps.goprocontrollerandroid.feature.media.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaItems(
    @SerialName("id")
    val id: String,
    @SerialName("media")
    val media: List<MediaDirectory>
)