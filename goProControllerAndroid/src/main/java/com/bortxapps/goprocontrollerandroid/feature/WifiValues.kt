package com.bortxapps.goprocontrollerandroid.feature

const val GOPRO_BASE_URL = "http://10.5.5.9:8080/"
const val GOPRO_BASE_PATH = "gopro"

enum class GoProResources(val url: String) {
    CAMERA("/camera"),
    MEDIA("/media"),
    VERSION("/version"),
    WEBCAM("/webcam")
}

const val GET_CAMERA_STATE = "/gopro/camera/state"
const val SET_CLIENT_INFO_URL = "/gopro/camera/analytics/set_client_info"
const val SET_DIGITAL_ZOOM_URL = "/gopro/camera/digital_zoom" // ?percent=50
const val GET_DATE_TIME_URL = "/gopro/camera/get_date_time"
const val KEEP_ALIVE_URL = "/gopro/camera/keep_alive"
const val GET_CAMERA_PRESETS_URL = "/gopro/camera/presets/get"
const val LOAD_CAMERA_PRESET_URL = "/gopro/camera/presets/load" // ?id=305441741
const val SET_CAMERA_PRESET_GROUP_URL = "/gopro/camera/presets/set_group" // ?id=1000 / ?id=1001 / ?id=1002
const val SET_UI_CONTROLLER_URL = "/gopro/camera/control/set_ui_controller" // ?p=0 / ?p=2
const val SET_DATE_TIME_URL = "/gopro/camera/set_date_time" // ?date=2023_1_31&time=3_4_5 / ?date=2023_1_31&time=3_4_5&tzone=-120&dst=1
const val START_SHUTTER_URL = "/gopro/camera/shutter/start"
const val STOP_SHUTTER_URL = "/gopro/camera/shutter/stop"
const val START_STREAM_URL = "/gopro/camera/stream/start"
const val STOP_STREAM_URL = "/gopro/camera/stream/stop"
const val WIRED_USB_P0_URL = "/gopro/camera/control/wired_usb?p=0"
const val WIRED_USB_P1_URL = "/gopro/camera/control/wired_usb?p=1"
const val SETTING_URL = "/gopro/camera/setting" // ?p=2&n=2&v=0 / ?p=2&n=3&v=0

const val GET_GPMF_URL = "/gopro/media/gpmf"
const val HILIGHT_FILE_URL = "/gopro/media/hilight/file" // ?path=100GOPRO/XXX.JPG / ?path=100GOPRO/XXX.MP4&ms=2500
const val HILIGHT_REMOVE_URL = "/gopro/media/hilight/remove" // ?path=100GOPRO/XXX.JPG / ?path=100GOPRO/XXX.MP4
const val HILIGHT_MOMENT_URL = "/gopro/media/hilight/moment"
const val GET_MEDIA_INFO_URL = "/gopro/media/info" // ?path=100GOPRO/XXX.JPG / ?path=100GOPRO/XXX.MP4
const val GET_MEDIA_LIST = "/gopro/media/list"
const val GET_SCREENNAIL_URL = "/gopro/media/screennail" // ?path=100GOPRO/XXX.JPG / ?path=100GOPRO/XXX.MP4
const val GET_TELEMETRY_URL = "/gopro/media/telemetry" // ?path=100GOPRO/XXX.JPG / ?path=100GOPRO/XXX.MP4
const val GET_THUMBNAIL_URL = "/gopro/media/thumbnail" // ?path=100GOPRO/XXX.JPG / ?path=100GOPRO/XXX.MP4
const val TURBO_TRANSFER_URL = "/gopro/media/turbo_transfer"

const val EXIT_WEBCAM_URL = "/gopro/webcam/exit"
const val PREVIEW_WEBCAM_URL = "/gopro/webcam/preview"
const val START_WEBCAM_URL = "/gopro/webcam/start"
const val START_WEBCAM_WITH_PORT_URL = "/gopro/webcam/start?port=12345"
const val START_WEBCAM_WITH_RES_URL = "/gopro/webcam/start?res=12&fov=0"
const val GET_WEBCAM_STATUS_URL = "/gopro/webcam/status"
const val STOP_WEBCAM_URL = "/gopro/webcam/stop"
const val GET_WEBCAM_VERSION_URL = "/gopro/webcam/version"

const val PRESET_GROUP_ID_VIDEO = "1000"
const val PRESET_GROUP_ID_PHOTO = "1001"
const val PRESET_GROUP_ID_TIMELAPSE = "1002"

const val QUERY_PARAM_SETTING_RESOLUTION_ID = "2"
const val QUERY_PARAM_SETTING_FRAME_RATE_ID = "3"