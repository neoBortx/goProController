package com.bortxapps.goprocontrollerandroid.feature.commands.api

import com.bortxapps.goprocontrollerandroid.feature.GET_CAMERA_PRESETS_URL
import com.bortxapps.goprocontrollerandroid.feature.GET_CAMERA_STATE
import com.bortxapps.goprocontrollerandroid.feature.GET_DATE_TIME_URL
import com.bortxapps.goprocontrollerandroid.feature.KEEP_ALIVE_URL
import com.bortxapps.goprocontrollerandroid.feature.PRESET_GROUP_ID_PHOTO
import com.bortxapps.goprocontrollerandroid.feature.PRESET_GROUP_ID_TIMELAPSE
import com.bortxapps.goprocontrollerandroid.feature.PRESET_GROUP_ID_VIDEO
import com.bortxapps.goprocontrollerandroid.feature.QUERY_PARAM_SETTING_FRAME_RATE
import com.bortxapps.goprocontrollerandroid.feature.QUERY_PARAM_SETTING_FRAME_RATE_ID
import com.bortxapps.goprocontrollerandroid.feature.QUERY_PARAM_SETTING_RESOLUTION_ID
import com.bortxapps.goprocontrollerandroid.feature.QUER_PARAM_SETTINGS_RESOLUTION
import com.bortxapps.goprocontrollerandroid.feature.SETTING_URL
import com.bortxapps.goprocontrollerandroid.feature.SET_CAMERA_PRESET_GROUP_URL
import com.bortxapps.goprocontrollerandroid.feature.SET_DATE_TIME_URL
import com.bortxapps.goprocontrollerandroid.feature.SET_DIGITAL_ZOOM_URL
import com.bortxapps.goprocontrollerandroid.feature.START_SHUTTER_URL
import com.bortxapps.goprocontrollerandroid.feature.START_STREAM_URL
import com.bortxapps.goprocontrollerandroid.feature.STOP_SHUTTER_URL
import com.bortxapps.goprocontrollerandroid.feature.STOP_STREAM_URL
import com.bortxapps.goprocontrollerandroid.feature.commands.data.CameraStatus
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.KtorClient
import java.util.Date

class CommandsApi(private val client: KtorClient = KtorClient()) {

    internal suspend fun getCameraState() =
        client.getRawRequest(path = GET_CAMERA_STATE)

    /**
     * @param value 0 to 100
     */
    internal suspend fun setDigitalZoom(zoom: Int) = client.getRawRequest(
        path = SET_DIGITAL_ZOOM_URL,
        queryParams = mapOf("percent" to zoom.toString())
    )

    internal suspend fun getDateTime() = client.getRawRequest(
        path = GET_DATE_TIME_URL
    )

    internal suspend fun setDateTime(date: Date) = client.getRawRequest(
        path = SET_DATE_TIME_URL,
        queryParams = mapOf(
            "date" to "$date.year_$date.month_$date.day",
            "time" to "$date.hours_$date.minutes_$date.seconds",
            "tzone" to "$date.timeZone.rawOffset",
            "dst" to "$date.timeZone.dstSavings"
        )
    )

    internal suspend fun getKeepAlive() = client.getRawRequest(
        path = KEEP_ALIVE_URL
    )

    internal suspend fun startCameraStream() = client.getRawRequest(
        path = START_STREAM_URL
    )

    internal suspend fun stopCameraStream() = client.getRawRequest(
        path = STOP_STREAM_URL
    )

    internal suspend fun startShutterStream() = client.getRawRequest(
        path = START_SHUTTER_URL
    )

    internal suspend fun stopShutterStream() = client.getRawRequest(
        path = STOP_SHUTTER_URL
    )

    internal suspend fun getCameraPresets() = client.getRawRequest(
        path = GET_CAMERA_PRESETS_URL
    )

    internal suspend fun setCameraVideoPresets() = client.sendRawPostRequest(
        path = SET_CAMERA_PRESET_GROUP_URL,
        queryParams = mapOf("id" to PRESET_GROUP_ID_VIDEO)
    )

    internal suspend fun setCameraPhotoPresets() = client.sendRawPostRequest(
        path = SET_CAMERA_PRESET_GROUP_URL,
        queryParams = mapOf("id" to PRESET_GROUP_ID_PHOTO)
    )

    internal suspend fun setCameraTimeLapsePresets() = client.sendRawPostRequest(
        path = SET_CAMERA_PRESET_GROUP_URL,
        queryParams = mapOf("id" to PRESET_GROUP_ID_TIMELAPSE)
    )

    internal suspend fun setFrameRate(res: QUERY_PARAM_SETTING_FRAME_RATE) = client.sendRawPostRequest(
        path = SETTING_URL,
        queryParams = mapOf("setting" to QUERY_PARAM_SETTING_FRAME_RATE_ID, "option" to res.id)
    )

    internal suspend fun setResolution(res: QUER_PARAM_SETTINGS_RESOLUTION) = client.sendRawPostRequest(
        path = SETTING_URL,
        queryParams = mapOf("setting" to QUERY_PARAM_SETTING_RESOLUTION_ID, "option" to res.id)
    )
}