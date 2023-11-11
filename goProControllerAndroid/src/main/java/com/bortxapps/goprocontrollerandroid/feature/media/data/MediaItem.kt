package com.bortxapps.goprocontrollerandroid.feature.media.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaItem(
    // ID of first member of a group (for grouped media items)
    @SerialName("b")
    val b: String,
    // Creation timestamp (seconds since epoch)
    @SerialName("cre")
    val creationTimeStamp: Long,
    // Group ID (if grouped media item)
    @SerialName("g")
    val groupId: String,
    // ID of last member of a group (for grouped media items)
    @SerialName("l")
    val l: String,
    // List of missing/deleted group member IDs (for grouped media items)
    @SerialName("m")
    val m: List<String>,
    // Last modified time (seconds since epoch)
    @SerialName("mod")
    val mod: Long,
    // Media filename
    @SerialName("n")
    val fileName: String,
    // Size of (group) media in bytes
    @SerialName("s")
    val size: String,
    // Group type (for grouped media items) (b -> burst, c -> continuous shot, n -> night lapse, t -> time lapse)
    @SerialName("t")
    val groupType: GroupType
)

@Serializable
enum class GroupType(val value: String) {
    @SerialName("b")
    BURST("b"),

    @SerialName("c")
    CONTINUOUS_SHOT("c"),

    @SerialName("n")
    NIGHT_LAPSE("n"),

    @SerialName("t")
    TIME_LAPSE("t")
}