package com.bortxapps.goprocontrollerandroid.feature.commands.data

const val QUERY_LENGTH_1: Byte = 0x01
const val QUERY_LENGTH_2: Byte = 0x02
const val QUERY_LENGTH_3: Byte = 0x03
const val QUERY_LENGTH_4: Byte = 0x04

const val RESOLUTION_CHARACTERISTIC_ID: Byte = 0x02
const val FRAME_RATE_CHARACTERISTIC_ID: Byte = 0x03
const val HYPER_SMOOTH_CHARACTERISTIC_ID: Byte = 0x87.toByte()
const val SPEED_CHARACTERISTIC_ID: Byte = 0xB0.toByte()
const val SHUTTER_CHARACTERISTIC_ID: Byte = 0x01
const val PRESETS_CHARACTERISTIC_ID: Byte = 0x3E

/**
 * First byte is the length of the command excluding itself
 * The rest of the content of each command is defined in the GoPro API
 *
 * https://gopro.github.io/OpenGoPro/ble_2_0#general
 */
enum class GoProBleCommands(val byteArray: ByteArray) {
    EnableWifi(byteArrayOf(QUERY_LENGTH_3, 0x17, 0x01, 0x01)),
    DisableWifi(byteArrayOf(QUERY_LENGTH_3, 0x17, 0x01, 0x00)),
    GetHardwareInfo(byteArrayOf(QUERY_LENGTH_1, 0x3C)),
    GetOpenGoProVersion(byteArrayOf(QUERY_LENGTH_1, 0x51)),
    GetCameraStatus(byteArrayOf(QUERY_LENGTH_1, 0x13)),

    // CONFIGURE SETTINGS
    SetPresetsVideo(byteArrayOf(QUERY_LENGTH_4, PRESETS_CHARACTERISTIC_ID, 0x02, 0x03, 0xE8.toByte())),
    SetPresetsPhoto(byteArrayOf(QUERY_LENGTH_4, PRESETS_CHARACTERISTIC_ID, 0x02, 0x03, 0xE9.toByte())),
    SetPresetsTimeLapse(byteArrayOf(QUERY_LENGTH_4, PRESETS_CHARACTERISTIC_ID, 0x02, 0x03, 0xEA.toByte())),


    SetResolution53K(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x64)),
    SetResolution4K(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x01)),
    SetResolution4K43(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x12)),
    SetResolution2K(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x04)),
    SetResolution2K43(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x06)),
    SetResolution1440(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x07)),
    SetResolution1080(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x09)),

    SetResolution5KGoPro12(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x65)),
    SetResolution4KGoPro12(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x66)),
    SetResolution4K43GoPro12(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x67)),
    SetResolution2KGoPro12(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x68)),
    SetResolution2K43GoPro12(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x69)),
    SetResolution1080GoPro12(byteArrayOf(QUERY_LENGTH_3, RESOLUTION_CHARACTERISTIC_ID, 0x01, 0x6A)),

    SetFrameRate240(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x00)),
    SetFrameRate200(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x0D)),
    SetFrameRate120(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x01)),
    SetFrameRate100(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x02)),
    SetFrameRate60(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x05)),
    SetFrameRate50(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x06)),
    SetFrameRate30(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x08)),
    SetFrameRate25(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x09)),
    SetFrameRate24(byteArrayOf(QUERY_LENGTH_3, FRAME_RATE_CHARACTERISTIC_ID, 0x01, 0x0A)),

    SetHyperSmoothOff(byteArrayOf(QUERY_LENGTH_3, HYPER_SMOOTH_CHARACTERISTIC_ID, 0x01, 0x00)),
    SetHyperSmoothLow(byteArrayOf(QUERY_LENGTH_3, HYPER_SMOOTH_CHARACTERISTIC_ID, 0x01, 0x01)),
    SetHyperSmoothHigh(byteArrayOf(QUERY_LENGTH_3, HYPER_SMOOTH_CHARACTERISTIC_ID, 0x01, 0x02)),
    SetHyperSmoothBoost(byteArrayOf(QUERY_LENGTH_3, HYPER_SMOOTH_CHARACTERISTIC_ID, 0x01, 0x03)),
    SetHyperSmoothAuto(byteArrayOf(QUERY_LENGTH_3, HYPER_SMOOTH_CHARACTERISTIC_ID, 0x01, 0x04)),
    SetHyperSmoothStandard(byteArrayOf(QUERY_LENGTH_3, HYPER_SMOOTH_CHARACTERISTIC_ID, 0x01, 0x64)),

    SetSpeedNormal(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x03)),
    SetSpeedSlow(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x02)),
    SetSpeedSuperSLowMo(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x01)),
    SetSpeedUltraSlowMo(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x00)),
    SetSpeedSlowMoLongBattery(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x12)),
    SetSpeedSuperSlowMoLongBattery(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x11)),
    SetSpeedUltraSlowMoLongBattery(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x10)),
    SetSpeedSlow4K(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x18)),
    SetSpeedSuperSLowMo27k(byteArrayOf(QUERY_LENGTH_3, SPEED_CHARACTERISTIC_ID, 0x01, 0x19)),

    SetShutterOff(byteArrayOf(QUERY_LENGTH_3, SHUTTER_CHARACTERISTIC_ID, 0x01, 0x00)),
    SetShutterOn(byteArrayOf(QUERY_LENGTH_3, SHUTTER_CHARACTERISTIC_ID, 0x01, 0x01)),


    // QUERY SETTINGS
    GetCameraSettings(byteArrayOf(QUERY_LENGTH_1, 0x12)),
    GetResolution(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x02)),
    GetFrameRate(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x03)),
    GetAutoPowerDown(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x59)),
    GetAspectRatio(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x6C)),
    GetVideoLapseDigitalLenses(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x79)),
    GetPhotoLapseDigitalLenses(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x7A)),
    GetTimeLapseDigitalLenses(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x7B)),
    GetMediaFormat(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x80.toByte())),
    GetAntiFlicker(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x86.toByte())),
    GetHyperSmooth(byteArrayOf(QUERY_LENGTH_2, 0x12, HYPER_SMOOTH_CHARACTERISTIC_ID.toByte())),
    GetHorizonLeveling(byteArrayOf(QUERY_LENGTH_2, 0x12, 0x96.toByte())),
    GetMaxLens(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xA2.toByte())),
    GetHindsight(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xA7.toByte())),
    GetInterval(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xAB.toByte())),
    GetDuration(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xAC.toByte())),
    GetVideoPerformanceMode(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xAD.toByte())),
    GetControls(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xAF.toByte())),
    GetSpeed(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB0.toByte())),
    GetNightPhoto(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB1.toByte())),
    GetWirelessBand(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB2.toByte())),
    GetTrailLength(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB3.toByte())),
    GetVideoMode(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB4.toByte())),
    GetVideoModeBitRate(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB6.toByte())),
    GetVideoModeBitDepth(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB7.toByte())),
    GetVideoModeProfiles(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB8.toByte())),
    GetVideoModeAspectRatio(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xB9.toByte())),
    GetVideoModeGoPro12(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xBA.toByte())),
    GetLapseMode(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xBB.toByte())),
    GetLapseModeAspectRatio(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xBC.toByte())),
    GetLapseModeMaxLensMod(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xBD.toByte())),
    GetLapseModeMaxLensModEnabled(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xBE.toByte())),
    GetPhotoMode(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xBF.toByte())),
    GetPhotoModeAspectRatio(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xC0.toByte())),
    GetFraming(byteArrayOf(QUERY_LENGTH_2, 0x12, 0xC1.toByte())),

    // QUERY STATUS
    GetCurrentMode(byteArrayOf(QUERY_LENGTH_2, 0x13, 0x59)),
    GetActivePresetGroup(byteArrayOf(QUERY_LENGTH_2, 0x13, 0x60)),
    GetActivePreset(byteArrayOf(QUERY_LENGTH_2, 0x13, 0x61));

}