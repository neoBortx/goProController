package com.bortxapps.goprocontrollerandroid.feature.commands.data

import android.util.Log
import java.nio.ByteBuffer

object CameraStatus {


    fun decodeStatus(map: Map<Byte, ByteArray>): Map<String, String> {
        val res = mutableMapOf<String, String>()
        map.keys.forEach {
            try {

                val key: CameraStatusIds = CameraStatusIds.fromValue(it.toString())
                map[it]?.let { value ->
                    res += key.name to cameraStatusEnumConversionMap[key]?.invoke(value).toString()
                }
            } catch (e: Exception) {
                Log.e("CameraStatus", "Error decoding key: $it, value: ${map[it]}")
            }
        }
        return res
    }

    enum class CameraStatusIds(val id: String) {
        // 1;Internal battery present;Is the system’s internal battery present?
        INTERNAL_BATTERY_PRESENT("1"),

        // 2;Internal battery level;Rough approximation of internal battery level in bars
        INTERNAL_BATTERY_LEVEL("2"),

        // 6;System hot;Is the system currently overheating?
        SYSTEM_HOT("6"),

        // 8;System busy;Is the camera busy?
        SYSTEM_BUSY("8"),

        // 9;Quick capture active;Is Quick Capture feature enabled?
        QUICK_CAPTURE_ACTIVE("9"),

        // 10;Encoding active;Is the system encoding right now?
        ENCODING_ACTIVE("10"),

        // 11;Lcd lock active;Is LCD lock active?
        LCD_LOCK_ACTIVE("11"),

        // 13;Video progress counter;When encoding video, this is the duration (seconds) of the video so far; 0 otherwise
        VIDEO_PROGRESS_COUNTER("13"),

        // 17;Enable;Are Wireless Connections enabled?
        ENABLE("17"),

        // 19;State;The pairing state of the camera
        STATE("19"),

        // 20;Type;The last type of pairing that the camera was engaged in
        TYPE("20"),

        // 21;Pair time;Time (milliseconds) since boot of last successful pairing complete action
        PAIR_TIME("21"),

        // 22;State;State of current scan for WiFi Access Points. Appears to only change for CAH-related scans
        STATE_SCAN_WIFI("22"),

        // 23;Scan time msec;The time, in milliseconds since boot that the WiFi Access Point scan completed
        SCAN_TIME_MSEC("23"),

        // 24;Provision status;WiFi AP provisioning state
        PROVISION_STATUS("24"),

        // 26;Remote control version;Wireless remote control version
        REMOTE_CONTROL_VERSION("26"),

        // 27;Remote control connected;Is a wireless remote control connected?
        REMOTE_CONTROL_CONNECTED("27"),

        // 28;Pairing;Wireless Pairing State
        PAIRING("28"),

        // 29;Wlan ssid;Provisioned WIFI AP SSID. On BLE connection, value is big-endian byte-encoded int
        WLAN_SSID("29"),

        // 30;Ap ssid;Camera’s WIFI SSID. On BLE connection, value is big-endian byte-encoded int
        AP_SSID("30"),

        // 31;App count;The number of wireless devices connected to the camera
        APP_COUNT("31"),

        // 32;Enable;Is Preview Stream enabled?
        ENABLE_PREVIEW_STREAM("32"),

        // 33;Sd status;Primary Storage Status
        SD_STATUS("33"),

        // 34;Remaining photos;How many photos can be taken before sdcard is full
        REMAINING_PHOTOS("34"),

        // 35;Remaining video time;How many minutes of video can be captured with current settings before sdcard is full
        REMAINING_VIDEO_TIME("35"),

        // 36;Num group photos;How many group photos can be taken with current settings before sdcard is full
        NUM_GROUP_PHOTOS("36"),

        // 37;Num group videos;Total number of group videos on sdcard

        NUM_GROUP_VIDEOS("37"),

        // 38;Num total photos;Total number of photos on sdcard

        NUM_TOTAL_PHOTOS("38"),

        // 39;Num total videos;Total number of videos on sdcard
        NUM_TOTAL_VIDEOS("39"),

        // 41;Ota status;The current status of Over The Air (OTA) update
        OTA_STATUS("41"),

        // 42;Download cancel request pending;Is there a pending request to cancel a firmware update download?
        DOWNLOAD_CANCEL_REQUEST_PENDING("42"),

        // 45;Camera locate active;Is locate camera feature active?
        CAMERA_LOCATE_ACTIVE("45"),

        // 49;Multi shot count down;The current timelapse interval countdown value (e.g. 5…4…3…2…1…)
        MULTI_SHOT_COUNT_DOWN("49"),

        // 54;Remaining space;Remaining space on the sdcard in Kilobytes
        REMAINING_SPACE_KB("54"),

        // 55;Supported;Is preview stream supported in current recording/mode/secondary-stream?
        SUPPORTED_PREVIEW_STREAM("55"),

        // 56;Wifi bars;WiFi signal strength in bars
        WIFI_BARS("56"),

        // 58;Num hilights;The number of hilights in encoding video (set to 0 when encoding stops)
        NUM_HILIGHTS("58"),

        // 59;Last hilight time msec;Time since boot (msec) of most recent hilight in encoding video (set to 0 when encoding stops)
        LAST_HILIGHT_TIME_MSEC("59"),

        // 60;Next poll msec;The min time between camera status updates (msec). Do not poll for status more often than this
        NEXT_POLL_MSEC("60"),

        // 64;Remaining timelapse time;How many min of Timelapse video can be captured with current settings before sdcard is full
        REMAINING_TIMELAPSE_TIME("64"),

        // 65;Exposure select type;Liveview Exposure Select Mode
        EXPOSURE_SELECT_TYPE("65"),

        // 66;Exposure select x;Liveview Exposure Select: y-coordinate (percent)
        EXPOSURE_SELECT_X("66"),

        // 67;Exposure select y;Liveview Exposure Select: y-coordinate (percent)
        EXPOSURE_SELECT_Y("67"),

        // 68;Gps status;Does the camera currently have a GPS lock?
        GPS_STATUS("68"),

        // 69;Ap state;Is the camera in AP Mode?
        AP_STATE("69"),

        // 70;Internal battery percentage;Internal battery level (percent)
        INTERNAL_BATTERY_PERCENTAGE("70"),

        // 74;Acc mic status;Microphone Accessory status
        ACC_MIC_STATUS("74"),

        // 75;Digital zoom;Digital Zoom level (percent)
        DIGITAL_ZOOM("75"),

        // 76;Wireless band;Wireless Band
        WIRELESS_BAND("76"),

        // 77;Digital zoom active;Is Digital Zoom feature available?

        DIGITAL_ZOOM_ACTIVE("77"),

        // 78;Mobile friendly video;Are current video settings mobile friendly? (related to video compression and frame rate)
        MOBILE_FRIENDLY_VIDEO("78"),

        // 79;First time use;Is the camera currently in First Time Use (FTU) UI flow?
        FIRST_TIME_USE("79"),

        // 81;Band 5ghz avail;Is 5GHz wireless band available?

        BAND_5GHZ_AVAIL("81"),

        // 82;System ready;Is the system ready to accept commands?
        SYSTEM_READY("82"),

        // 83;Batt okay for ota;Is the internal battery charged sufficiently to start Over The Air (OTA) update?
        BATT_OKAY_FOR_OTA("83"),

        // 85;Video low temp alert;Is the camera getting too cold to continue recording?
        VIDEO_LOW_TEMP_ALERT("85"),

        // 86;Actual orientation;The rotational orientation of the camera
        ACTUAL_ORIENTATION("86"),

        // 88;Zoom while encoding;Is this camera capable of zooming while encoding (static value based on model, not settings)
        ZOOM_WHILE_ENCODING("88"),

        // 89;Current mode;Current flat mode ID
        CURRENT_MODE("89"),

        // 93;Active video presets;Current Video Preset (ID)
        ACTIVE_VIDEO_PRESETS("93"),

        // 94;Active photo presets;Current Photo Preset (ID)
        ACTIVE_PHOTO_PRESETS("94"),

        // 95;Active timelapse presets;Current Timelapse Preset (ID)
        ACTIVE_TIMELAPSE_PRESETS("95"),

        // 96;Active presets group;Current Preset Group (ID)
        ACTIVE_PRESETS_GROUP("96"),

        // 97;Active preset;Current Preset (ID)
        ACTIVE_PRESET("97"),

        // 98;Preset modified;Preset Modified Status, which contains an event ID and a preset (group) ID
        PRESET_MODIFIED("98"),

        // 99;Remaining live bursts;How many Live Bursts can be captured before sdcard is full
        REMAINING_LIVE_BURSTS("99"),

        // 100;Num total live bursts;Total number of Live Bursts on sdcard
        NUM_TOTAL_LIVE_BURSTS("100"),

        // 101;Capture delay active;Is Capture Delay currently active (i.e. counting down)?
        CAPTURE_DELAY_ACTIVE("101"),

        // 102;Media mod mic status;Media Mod State
        MEDIA_MOD_MIC_STATUS("102"),

        // 103;Timewarp speed ramp active;Time Warp Speed
        TIMEWARP_SPEED_RAMP_ACTIVE("103"),

        // 104;Linux core active;Is the system’s Linux core active?
        LINUX_CORE_ACTIVE("104"),

        // 105;Camera lens type;Camera lens type (reflects changes to setting 162 or setting 189)
        CAMERA_LENS_TYPE("105"),

        // 106;Video hindsight capture active;Is Video Hindsight Capture Active?
        VIDEO_HINDSIGHT_CAPTURE_ACTIVE("106"),

        // 107;Scheduled preset;Scheduled Capture Preset ID
        SCHEDULED_PRESET("107"),

        // 108;Scheduled enabled;Is Scheduled Capture set?
        SCHEDULED_ENABLED("108"),

        // 110;Media mod status;Media Mode Status (bitmasked)
        MEDIA_MOD_STATUS("110"),

        // 111;Sd rating check error;Does sdcard meet specified minimum write speed?
        SD_RATING_CHECK_ERROR("111"),

        // 112;Sd write speed error;Number of sdcard write speed errors since device booted
        SD_WRITE_SPEED_ERROR("112"),

        // 113;Turbo transfer;Is Turbo Transfer active?
        TURBO_TRANSFER("113"),

        // 114;Camera control status;Camera control status ID
        CAMERA_CONTROL_STATUS("114"),

        // 115;Usb connected;Is the camera connected to a PC via USB?
        USB_CONNECTED("115"),

        // 116;Allow control over usb;Camera control over USB state
        ALLOW_CONTROL_OVER_USB("116"),

        // 117;Total sd space kb;Total SD card capacity in Kilobyte
        TOTAL_SD_SPACE_KB("117");

        companion object {
            fun fromValue(value: String): CameraStatusIds = values().first { it.id == value }
        }
    }

    enum class InternalBatteryLevel(val id: String) {
        ZERO("0"),
        ONE("1"),
        TWO("2"),
        THREE("3"),
        FOUR("4");

        companion object {

            fun fromValue(value: ByteArray): InternalBatteryLevel {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> ZERO
                    "1" -> ONE
                    "2" -> TWO
                    "3" -> THREE
                    "4" -> FOUR
                    else -> throw IllegalArgumentException("Invalid value for InternalBatteryLevel: $value")
                }
            }
        }
    }

    enum class PairingState(val value: String) {
        NEVER_PAIRED("0"),
        STARTED("1"),
        ABORTED("2"),
        CANCELLED(""),
        COMPLETED("4");

        companion object {

            fun fromValue(value: ByteArray): PairingState {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> NEVER_PAIRED
                    "1" -> STARTED
                    "2" -> ABORTED
                    "3" -> CANCELLED
                    "4" -> COMPLETED
                    else -> throw IllegalArgumentException("Invalid value for PairingState: $value")
                }
            }
        }
    }

    enum class LastTypePairing(val value: String) {
        NEVER_PAIRED("0"),
        PAIRING_APP("1"),
        PAIRING_REMOTE_CONTROL("2"),
        PAIRING_BLUETOOTH_DEVICE("3");

        companion object {

            fun fromValue(value: ByteArray): LastTypePairing {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> NEVER_PAIRED
                    "1" -> PAIRING_APP
                    "2" -> PAIRING_REMOTE_CONTROL
                    "3" -> PAIRING_BLUETOOTH_DEVICE
                    else -> throw IllegalArgumentException("Invalid value for LastTypePairing: $value")
                }
            }
        }
    }

    enum class CurrentScanState(val value: String) {
        NEVER_STARTED("0"),
        STARTED("1"),
        ABORTED("2"),
        CANCELED("3"),
        COMPLETED("4");

        companion object {

            fun fromValue(value: ByteArray): CurrentScanState {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> NEVER_STARTED
                    "1" -> STARTED
                    "2" -> ABORTED
                    "3" -> CANCELED
                    "4" -> COMPLETED
                    else -> throw IllegalArgumentException("Invalid value for CurrentScanState: $value")
                }
            }
        }
    }

    enum class WiFiAPProvisioningState(val value: String) {

        NEVER_STARTED("0"),
        STARTED("1"),
        ABORTED("3"),
        CANCELED("4"),
        COMPLETED("5");

        companion object {

            fun fromValue(value: ByteArray): WiFiAPProvisioningState {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> NEVER_STARTED
                    "1" -> STARTED
                    "2" -> ABORTED
                    "3" -> CANCELED
                    "4" -> COMPLETED
                    else -> throw IllegalArgumentException("Invalid value for WiFiAPProvisioningState: $value")
                }
            }
        }
    }

    enum class PrimaryStorageStatus(val value: String) {
        UNKNOWN("-1"),
        OK("0"),
        SD_CARD_FULL("1"),
        SD_CARD_REMOVED("2"),
        SD_CARD_FORMAT_ERROR("3"),
        SD_CARD_BUSY("4"),
        SD_CARD_SWAPPED("8");

        companion object {

            fun fromValue(value: ByteArray): PrimaryStorageStatus {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "-1" -> UNKNOWN
                    "0" -> OK
                    "1" -> SD_CARD_FULL
                    "2" -> SD_CARD_REMOVED
                    "3" -> SD_CARD_FORMAT_ERROR
                    "4" -> SD_CARD_BUSY
                    "8" -> SD_CARD_SWAPPED
                    else -> throw IllegalArgumentException("Invalid value for PrimaryStorageStatus: $value")
                }
            }
        }
    }

    enum class OTAUpdateStatus(val value: String) {
        IDLE("0"),
        DOWNLOADING("1"),
        VERIFYING("2"),
        DOWNLOAD_FAILED("3"),
        VERIFY_FAILED("4"),
        READY("5"),
        GOPRO_APP_DOWNLOADING("6"),
        GOPRO_APP_VERIFYING("7"),
        GOPRO_APP_DOWNLOAD_FAILED("8"),
        GOPRO_APP_VERIFY_FAILED("9"),
        GOPRO_APP_READY("10");

        companion object {

            fun fromValue(value: ByteArray): OTAUpdateStatus {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> IDLE
                    "1" -> DOWNLOADING
                    "2" -> VERIFYING
                    "3" -> DOWNLOAD_FAILED
                    "4" -> VERIFY_FAILED
                    "5" -> READY
                    "6" -> GOPRO_APP_DOWNLOADING
                    "7" -> GOPRO_APP_VERIFYING
                    "8" -> GOPRO_APP_DOWNLOAD_FAILED
                    "9" -> GOPRO_APP_VERIFY_FAILED
                    "10" -> GOPRO_APP_READY
                    else -> throw IllegalArgumentException("Invalid value for OTAUpdateStatus: $value")
                }
            }
        }
    }

    enum class LiveViewExposureSelectMode(val value: String) {
        DISABLED("0"),
        AUTO("1"),
        ISO_LOCK("2"),
        HEMISPHERE("3");

        companion object {

            fun fromValue(value: ByteArray): LiveViewExposureSelectMode {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> DISABLED
                    "1" -> AUTO
                    "2" -> ISO_LOCK
                    "3" -> HEMISPHERE
                    else -> throw IllegalArgumentException("Invalid value for LiveviewExposureSelectMode: $value")
                }
            }
        }
    }

    enum class MicrophoneAccessoryStatus(val value: String) {
        NOT_CONNECTED("0"),
        CONNECTED("1"),
        CONNECTED_WITH_MICROPHONE("2");

        companion object {

            fun fromValue(value: ByteArray): MicrophoneAccessoryStatus {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> NOT_CONNECTED
                    "1" -> CONNECTED
                    "2" -> CONNECTED_WITH_MICROPHONE
                    else -> throw IllegalArgumentException("Invalid value for MicrophoneAccessoryStatus: $value")
                }
            }
        }
    }

    enum class WirelessBand(val value: String) {
        BAND_2_4_GHZ("0"),
        BAND_5_GHZ("1"),
        MAX("2");

        companion object {

            fun fromValue(value: ByteArray): WirelessBand {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> BAND_2_4_GHZ
                    "1" -> BAND_5_GHZ
                    "2" -> MAX
                    else -> throw IllegalArgumentException("Invalid value for WirelessBand: $value")
                }
            }
        }
    }

    enum class CameraRotationalOrientation(val value: String) {
        // 0: 0 degrees (upright)
        DEGREES_0("0"),

        // 1: 180 degrees (upside down)
        DEGREES_180("1"),

        // 2: 90 degrees (laying on right side)
        DEGREES_90("2"),

        // 3: 270 degrees (laying on left side)
        DEGREES_270("3");

        companion object {

            fun fromValue(value: ByteArray): CameraRotationalOrientation {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> DEGREES_0
                    "1" -> DEGREES_180
                    "2" -> DEGREES_90
                    "3" -> DEGREES_270
                    else -> throw IllegalArgumentException("Invalid value for CameraRotationalOrientation: $value")
                }
            }
        }
    }

    enum class MediaModState(val value: String) {
        // 0: Media mod microphone removed
        MICROPHONE_REMOVED("0"),

        // 2: Media mod microphone only
        MICROPHONE_ONLY("2"),

        // 3: Media mod microphone with external microphone

        MICROPHONE_WITH_EXTERNAL_MIC("3");

        companion object {

            fun fromValue(value: ByteArray): MediaModState {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> MICROPHONE_REMOVED
                    "2" -> MICROPHONE_ONLY
                    "3" -> MICROPHONE_WITH_EXTERNAL_MIC
                    else -> throw IllegalArgumentException("Invalid value for MediaModState: $value")
                }
            }
        }
    }

    enum class TimeWarpSpeed(val value: String) {
        SPEED_15X("0"),
        SPEED_30X("1"),
        SPEED_60X("2"),
        SPEED_150X("3"),
        SPEED_300X("4"),
        SPEED_900X("5"),
        SPEED_1800X("6"),
        SPEED_2X("7"),
        SPEED_5X("8"),
        SPEED_10X("9"),
        AUTO("10"),
        REALTIME("11"),
        SLOW_MOTION("12");

        companion object {

            fun fromValue(value: ByteArray): TimeWarpSpeed {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> SPEED_15X
                    "1" -> SPEED_30X
                    "2" -> SPEED_60X
                    "3" -> SPEED_150X
                    "4" -> SPEED_300X
                    "5" -> SPEED_900X
                    "6" -> SPEED_1800X
                    "7" -> SPEED_2X
                    "8" -> SPEED_5X
                    "9" -> SPEED_10X
                    "10" -> AUTO
                    "11" -> REALTIME
                    "12" -> SLOW_MOTION
                    else -> throw IllegalArgumentException("Invalid value for TimeWarpSpeed: $value")
                }
            }
        }
    }

    enum class CameraLensType(val value: String) {
        DEFAULT("0"),
        MAX_LENS("1"),
        MAX_LENS_2_0("2");

        companion object {

            fun fromValue(value: ByteArray): CameraLensType {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> DEFAULT
                    "1" -> MAX_LENS
                    "2" -> MAX_LENS_2_0
                    else -> throw IllegalArgumentException("Invalid value for CameraLensType: $value")
                }
            }
        }
    }

    enum class MediaModeStatus(val value: String) {
        SELFIE_MODE_0_HDMI_0_MEDIA_MOD_FALSE("0"),
        SELFIE_MODE_0_HDMI_0_MEDIA_MOD_TRUE("1"),
        SELFIE_MODE_0_HDMI_1_MEDIA_MOD_FALSE("2"),
        SELFIE_MODE_0_HDMI_1_MEDIA_MOD_TRUE("3"),
        SELFIE_MODE_1_HDMI_0_MEDIA_MOD_FALSE("4"),
        SELFIE_MODE_1_HDMI_0_MEDIA_MOD_TRUE("5"),
        SELFIE_MODE_1_HDMI_1_MEDIA_MOD_FALSE("6"),
        SELFIE_MODE_1_HDMI_1_MEDIA_MOD_TRUE("7");

        companion object {

            fun fromValue(value: ByteArray): MediaModeStatus {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> SELFIE_MODE_0_HDMI_0_MEDIA_MOD_FALSE
                    "1" -> SELFIE_MODE_0_HDMI_0_MEDIA_MOD_TRUE
                    "2" -> SELFIE_MODE_0_HDMI_1_MEDIA_MOD_FALSE
                    "3" -> SELFIE_MODE_0_HDMI_1_MEDIA_MOD_TRUE
                    "4" -> SELFIE_MODE_1_HDMI_0_MEDIA_MOD_FALSE
                    "5" -> SELFIE_MODE_1_HDMI_0_MEDIA_MOD_TRUE
                    "6" -> SELFIE_MODE_1_HDMI_1_MEDIA_MOD_FALSE
                    "7" -> SELFIE_MODE_1_HDMI_1_MEDIA_MOD_TRUE
                    else -> throw IllegalArgumentException("Invalid value for MediaModeStatus: $value")
                }
            }
        }
    }

    enum class CameraControlStatus(val value: String) {
        CAMERA_IDLE("0"),
        CAMERA_CONTROL("1"),
        CAMERA_EXTERNAL_CONTROL("2");

        companion object {

            fun fromValue(value: ByteArray): CameraControlStatus {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> CAMERA_IDLE
                    "1" -> CAMERA_CONTROL
                    "2" -> CAMERA_EXTERNAL_CONTROL
                    else -> throw IllegalArgumentException("Invalid value for CameraControlStatus: $value")
                }
            }
        }
    }

    enum class CameraControlOverUSBState(val value: String) {
        DISABLED("0"),
        ENABLED("1");

        companion object {

            fun fromValue(value: ByteArray): CameraControlOverUSBState {
                return when (ByteBuffer.wrap(value)[0].toInt().toString()) {
                    "0" -> DISABLED
                    "1" -> ENABLED
                    else -> throw IllegalArgumentException("Invalid value for CameraControlOverUSBState: $value")
                }
            }
        }
    }

    private const val INTEGER_BYTES_LENGTH = 4


    private fun getValueInt(value: ByteArray): Int = if (value.size == INTEGER_BYTES_LENGTH) {
        ByteBuffer.wrap(value).getInt()
    } else {
        ByteBuffer.wrap(value)[0].toInt()
    }


    private fun getValueString(value: ByteArray): String = String(value, Charsets.UTF_8)


    private fun getValueBoolean(value: ByteArray): Boolean = ByteBuffer.wrap(value)[0].toInt() == 1


    val cameraStatusEnumConversionMap = mapOf(
        CameraStatusIds.INTERNAL_BATTERY_PRESENT to ::getValueBoolean,
        CameraStatusIds.INTERNAL_BATTERY_LEVEL to InternalBatteryLevel::fromValue,
        CameraStatusIds.SYSTEM_HOT to ::getValueBoolean,
        CameraStatusIds.SYSTEM_BUSY to ::getValueBoolean,
        CameraStatusIds.QUICK_CAPTURE_ACTIVE to ::getValueBoolean,
        CameraStatusIds.ENCODING_ACTIVE to ::getValueBoolean,
        CameraStatusIds.LCD_LOCK_ACTIVE to ::getValueBoolean,
        CameraStatusIds.VIDEO_PROGRESS_COUNTER to ::getValueInt,
        CameraStatusIds.ENABLE to ::getValueBoolean,
        CameraStatusIds.STATE to PairingState::fromValue,
        CameraStatusIds.TYPE to LastTypePairing::fromValue,
        CameraStatusIds.PAIR_TIME to ::getValueInt,
        CameraStatusIds.STATE_SCAN_WIFI to CurrentScanState::fromValue,
        CameraStatusIds.SCAN_TIME_MSEC to ::getValueInt,
        CameraStatusIds.PROVISION_STATUS to WiFiAPProvisioningState::fromValue,
        CameraStatusIds.REMOTE_CONTROL_VERSION to ::getValueInt,
        CameraStatusIds.REMOTE_CONTROL_CONNECTED to ::getValueBoolean,
        CameraStatusIds.PAIRING to ::getValueInt,
        CameraStatusIds.WLAN_SSID to ::getValueString,
        CameraStatusIds.AP_SSID to ::getValueString,
        CameraStatusIds.APP_COUNT to ::getValueInt,
        CameraStatusIds.ENABLE_PREVIEW_STREAM to ::getValueBoolean,
        CameraStatusIds.SD_STATUS to PrimaryStorageStatus::fromValue,
        CameraStatusIds.REMAINING_PHOTOS to ::getValueInt,
        CameraStatusIds.REMAINING_VIDEO_TIME to ::getValueInt,
        CameraStatusIds.NUM_GROUP_PHOTOS to ::getValueInt,
        CameraStatusIds.NUM_GROUP_VIDEOS to ::getValueInt,
        CameraStatusIds.NUM_TOTAL_PHOTOS to ::getValueInt,
        CameraStatusIds.NUM_TOTAL_VIDEOS to ::getValueInt,
        CameraStatusIds.OTA_STATUS to OTAUpdateStatus::fromValue,
        CameraStatusIds.DOWNLOAD_CANCEL_REQUEST_PENDING to ::getValueBoolean,
        CameraStatusIds.CAMERA_LOCATE_ACTIVE to ::getValueBoolean,
        CameraStatusIds.MULTI_SHOT_COUNT_DOWN to ::getValueInt,
        CameraStatusIds.REMAINING_SPACE_KB to ::getValueInt,
        CameraStatusIds.SUPPORTED_PREVIEW_STREAM to ::getValueBoolean,
        CameraStatusIds.WIFI_BARS to ::getValueInt,
        CameraStatusIds.NUM_HILIGHTS to ::getValueInt,
        CameraStatusIds.LAST_HILIGHT_TIME_MSEC to ::getValueInt,
        CameraStatusIds.NEXT_POLL_MSEC to ::getValueInt,
        CameraStatusIds.REMAINING_TIMELAPSE_TIME to ::getValueInt,
        CameraStatusIds.EXPOSURE_SELECT_TYPE to LiveViewExposureSelectMode::fromValue,
        CameraStatusIds.EXPOSURE_SELECT_X to ::getValueInt,
        CameraStatusIds.EXPOSURE_SELECT_Y to ::getValueInt,
        CameraStatusIds.GPS_STATUS to ::getValueBoolean,
        CameraStatusIds.AP_STATE to ::getValueBoolean,
        CameraStatusIds.INTERNAL_BATTERY_PERCENTAGE to ::getValueInt,
        CameraStatusIds.ACC_MIC_STATUS to MicrophoneAccessoryStatus::fromValue,
        CameraStatusIds.DIGITAL_ZOOM to ::getValueInt,
        CameraStatusIds.WIRELESS_BAND to WirelessBand::fromValue,
        CameraStatusIds.DIGITAL_ZOOM_ACTIVE to ::getValueBoolean,
        CameraStatusIds.MOBILE_FRIENDLY_VIDEO to ::getValueBoolean,
        CameraStatusIds.FIRST_TIME_USE to ::getValueBoolean,
        CameraStatusIds.BAND_5GHZ_AVAIL to ::getValueBoolean,
        CameraStatusIds.SYSTEM_READY to ::getValueBoolean,
        CameraStatusIds.BATT_OKAY_FOR_OTA to ::getValueBoolean,
        CameraStatusIds.VIDEO_LOW_TEMP_ALERT to ::getValueBoolean,
        CameraStatusIds.ACTUAL_ORIENTATION to CameraRotationalOrientation::fromValue,
        CameraStatusIds.ZOOM_WHILE_ENCODING to ::getValueBoolean,
        CameraStatusIds.CURRENT_MODE to ::getValueInt,
        CameraStatusIds.ACTIVE_VIDEO_PRESETS to ::getValueInt,
        CameraStatusIds.ACTIVE_PHOTO_PRESETS to ::getValueInt,
        CameraStatusIds.ACTIVE_TIMELAPSE_PRESETS to ::getValueInt,
        CameraStatusIds.ACTIVE_PRESETS_GROUP to ::getValueInt,
        CameraStatusIds.ACTIVE_PRESET to ::getValueInt,
        CameraStatusIds.PRESET_MODIFIED to ::getValueInt,
        CameraStatusIds.REMAINING_LIVE_BURSTS to ::getValueInt,
        CameraStatusIds.NUM_TOTAL_LIVE_BURSTS to ::getValueInt,
        CameraStatusIds.CAPTURE_DELAY_ACTIVE to ::getValueBoolean,
        CameraStatusIds.MEDIA_MOD_MIC_STATUS to MediaModState::fromValue,
        CameraStatusIds.TIMEWARP_SPEED_RAMP_ACTIVE to TimeWarpSpeed::fromValue,
        CameraStatusIds.LINUX_CORE_ACTIVE to ::getValueBoolean,
        CameraStatusIds.CAMERA_LENS_TYPE to CameraLensType::fromValue,
        CameraStatusIds.VIDEO_HINDSIGHT_CAPTURE_ACTIVE to ::getValueBoolean,
        CameraStatusIds.SCHEDULED_PRESET to ::getValueInt,
        CameraStatusIds.SCHEDULED_ENABLED to ::getValueBoolean,
        CameraStatusIds.MEDIA_MOD_STATUS to MediaModeStatus::fromValue,
        CameraStatusIds.SD_RATING_CHECK_ERROR to ::getValueBoolean,
        CameraStatusIds.SD_WRITE_SPEED_ERROR to ::getValueInt,
        CameraStatusIds.TURBO_TRANSFER to ::getValueBoolean,
        CameraStatusIds.CAMERA_CONTROL_STATUS to CameraControlStatus::fromValue,
        CameraStatusIds.USB_CONNECTED to ::getValueBoolean,
        CameraStatusIds.ALLOW_CONTROL_OVER_USB to CameraControlOverUSBState::fromValue,
        CameraStatusIds.TOTAL_SD_SPACE_KB to ::getValueInt
    )
}