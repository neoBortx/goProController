package com.bortxapps.goprocontrollerandroid.feature.media.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaInfo(
    @SerialName("ao")
    val ao: AudioOption? = null, // Audio Option (off, stereo, wind, auto)
    @SerialName("avc_profile")
    val avc_profile: UByte? = null, // Advanced Video Codec Profile (0..255)
    @SerialName("cl")
    val cl: Boolean? = null, // File clipped from another source? (0:false, 1:true)
    @SerialName("cre")
    val cre: UInt? = null, // File creation timestamp (sec since epoch)
    @SerialName("ct")
    val ct: ContentType? = null, // Content type (0..12)
    @SerialName("dur")
    val dur: UInt? = null, // Duration of video in seconds
    @SerialName("eis")
    val eis: Boolean? = null, // File made with Electronic Image Stabilization (0:false, 1:true)
    @SerialName("fps")
    val fps: UInt? = null, // Frame rate (numerator)
    @SerialName("fps_denom")
    val fps_denom: UInt? = null, // Frame rate (denominator)
    @SerialName("gumi")
    val gumi: String? = null, // Globally Unique Media ID
    @SerialName("h")
    val h: UInt? = null, // Video height in pixels (1080)
    @SerialName("hc")
    val hc: UInt? = null, // Hilight count (video:0..99, photo:0..1)
    @SerialName("hdr")
    val hdr: Boolean? = null, // Photo taken with High Dynamic Range? (0:false, 1:true)
    @SerialName("hi")
    val hi: List<UInt>? = null, // Offset to hilights in media in milliseconds ([1500, 4700])
    @SerialName("lc")
    val lc: UInt? = null, // Spherical Lens Config (0:front, 1:rear)
    @SerialName("ls")
    val ls: Int? = null, // Low Resolution Video file size in bytes (or -1 if no LRV file) (-1, 1234567890)
    @SerialName("mos")
    val mos: List<MobileOffloadState>? = null, // Mobile Offload State ("app", "pc", "other")
    @SerialName("mp")
    val mp: Boolean? = null, // Metadata Present? (0:no metadata, 1:metadata exists)
    @SerialName("profile")
    val profile: UByte? = null, // Advanced Video Codec Level (0..255)
    @SerialName("prog")
    val prog: Boolean? = null, // Is video progressive? (0:interlaced, 1:progressive)
    @SerialName("pta")
    val pta: Boolean? = null, // Media has Protune audio file? (0:false, 1:true)
    @SerialName("raw")
    val raw: Boolean? = null, // Photo has raw version? (0:false, 1:true)
    @SerialName("s")
    val s: ULong? = null, // File size in bytes
    @SerialName("subsample")
    val subsample: Boolean? = null, // Is video subsampled? (0:false, 1:true)
    @SerialName("tr")
    val tr: Boolean? = null, // Is file transcoded? (0:false, 1:true)
    @SerialName("w")
    val w: UInt? = null, // Width of media in pixels (1920)
    @SerialName("wdr")
    val wdr: Boolean // Photo taken with Wide Dynamic Range? (0:false, 1:true)
)

@Serializable
enum class AudioOption {
    @SerialName("off")
    OFF,

    @SerialName("stereo")
    STEREO,

    @SerialName("wind")
    WIND,

    @SerialName("auto")
    AUTO;
}

@Serializable
enum class MobileOffloadState {
    @SerialName("app")
    APP,

    @SerialName("pc")
    PC,

    @SerialName("other")
    OTHER
}
enum class ContentType {
    @SerialName("0")
    VIDEO,

    @SerialName("1")
    LOOPING,

    @SerialName("2")
    CHAPTERED_VIDEO,

    @SerialName("3")
    TIME_LAPSE,

    @SerialName("4")
    SINGLE_PHOTO,

    @SerialName("5")
    BURST_PHOTO,

    @SerialName("6")
    TIME_LAPSE_PHOTO,

    @SerialName("8")
    NIGHT_LAPSE_PHOTO,

    @SerialName("9")
    NIGHT_PHOTO,

    @SerialName("10")
    CONTINUOUS_PHOTO,

    @SerialName("11")
    RAW_PHOTO,

    @SerialName("12")
    LIVE_BURST
}