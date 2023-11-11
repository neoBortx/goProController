package com.bortxapps.goprocontrollerandroid.feature.commands.data

/**
 * First byte is the length of the command excluding itself
 * The rest of the content of each command is defined in the GoPro API
 *
 * https://gopro.github.io/OpenGoPro/ble_2_0#general
 */
enum class GoProBleCommands(val byteArray: ByteArray) {
    EnableWifi(byteArrayOf(0x03, 0x17, 0x01, 0x01)),
    DisableWifi(byteArrayOf(0x03, 0x17, 0x01, 0x00)),
    GetHardwareInfo(byteArrayOf(0x01, 0x3C)),
    GetOpenGoProVersion(byteArrayOf(0x01, 0x51)),
    GetCameraStatus(byteArrayOf(0x01, 0x13)),
    SetPresetsVideo(byteArrayOf(0x04, 0x3E, 0x02, 0x03, 0xE8.toByte())),
    SetPresetsPhoto(byteArrayOf(0x04, 0x3E, 0x02, 0x03, 0xE9.toByte())),
    SetPresetsTimeLapse(byteArrayOf(0x04, 0x3E, 0x02, 0x03, 0xEA.toByte())),

    // CONFIGURE SETTINGS
    SetResolution53K(byteArrayOf(0x03, 0x02, 0x01, 0x64)),
    SetResolution4K(byteArrayOf(0x03, 0x02, 0x01, 0x01)),
    SetResolution4K43(byteArrayOf(0x03, 0x02, 0x01, 0x12)),
    SetResolution2K(byteArrayOf(0x03, 0x02, 0x01, 0x04)),
    SetResolution2K43(byteArrayOf(0x03, 0x02, 0x01, 0x06)),
    SetResolution1440(byteArrayOf(0x03, 0x02, 0x01, 0x07)),
    SetResolution1080(byteArrayOf(0x03, 0x02, 0x01, 0x09)),

    SetResolution5KGoPro12(byteArrayOf(0x03, 0x02, 0x01, 0x65)),
    SetResolution4KGoPro12(byteArrayOf(0x03, 0x02, 0x01, 0x66)),
    SetResolution4K43GoPro12(byteArrayOf(0x03, 0x02, 0x01, 0x67)),
    SetResolution2KGoPro12(byteArrayOf(0x03, 0x02, 0x01, 0x68)),
    SetResolution2K43GoPro12(byteArrayOf(0x03, 0x02, 0x01, 0x69)),
    SetResolution1080GoPro12(byteArrayOf(0x03, 0x02, 0x01, 0x6A)),

    SetFrameRate240(byteArrayOf(0x03, 0x03, 0x01, 0x00)),
    SetFrameRate200(byteArrayOf(0x03, 0x03, 0x01, 0x0D)),
    SetFrameRate120(byteArrayOf(0x03, 0x03, 0x01, 0x01)),
    SetFrameRate100(byteArrayOf(0x03, 0x03, 0x01, 0x02)),
    SetFrameRate60(byteArrayOf(0x03, 0x03, 0x01, 0x05)),
    SetFrameRate50(byteArrayOf(0x03, 0x03, 0x01, 0x06)),
    SetFrameRate30(byteArrayOf(0x03, 0x03, 0x01, 0x08)),
    SetFrameRate25(byteArrayOf(0x03, 0x03, 0x01, 0x09)),
    SetFrameRate24(byteArrayOf(0x03, 0x03, 0x01, 0x0A)),

    SetHyperSmoothOff(byteArrayOf(0x03, 0x87.toByte(), 0x01, 0x00)),
    SetHyperSmoothLow(byteArrayOf(0x03, 0x87.toByte(), 0x01, 0x01)),
    SetHyperSmoothHigh(byteArrayOf(0x03, 0x87.toByte(), 0x01, 0x02)),
    SetHyperSmoothBoost(byteArrayOf(0x03, 0x87.toByte(), 0x01, 0x03)),
    SetHyperSmoothAuto(byteArrayOf(0x03, 0x87.toByte(), 0x01, 0x04)),
    SetHyperSmoothStandard(byteArrayOf(0x03, 0x87.toByte(), 0x01, 0x64)),

    SetSpeedNormal(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x03)),
    SetSpeedSlow(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x02)),
    SetSpeedSuperSLowMo(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x01)),
    SetSpeedUltraSlowMo(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x00)),
    SetSpeedSlowMoLongBattery(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x12)),
    SetSpeedSuperSlowMoLongBattery(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x11)),
    SetSpeedUltraSlowMoLongBattery(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x10)),
    SetSpeedSlow4K(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x18)),
    SetSpeedSuperSLowMo27k(byteArrayOf(0x03, 0xB0.toByte(), 0x01, 0x19)),

    // QUERY SETTINGS
    GetCameraSettings(byteArrayOf(0x01, 0x12)),
    GetResolution(byteArrayOf(0x02, 0x12, 0x02)),
    GetFrameRate(byteArrayOf(0x02, 0x12, 0x03)),
    GetAutoPowerDown(byteArrayOf(0x02, 0x12, 0x59)),
    GetAspectRatio(byteArrayOf(0x02, 0x12, 0x6C)),
    GetVideoLapseDigitalLenses(byteArrayOf(0x02, 0x12, 0x79)),
    GetPhotoLapseDigitalLenses(byteArrayOf(0x02, 0x12, 0x7A)),
    GetTimeLapseDigitalLenses(byteArrayOf(0x02, 0x12, 0x7B)),
    GetMediaFormat(byteArrayOf(0x02, 0x12, 0x80.toByte())),
    GetAntiFlicker(byteArrayOf(0x02, 0x12, 0x86.toByte())),
    GetHyperSmooth(byteArrayOf(0x02, 0x12, 0x87.toByte())),
    GetHorizonLeveling(byteArrayOf(0x02, 0x12, 0x96.toByte())),
    GetMaxLens(byteArrayOf(0x02, 0x12, 0xA2.toByte())),
    GetHindsight(byteArrayOf(0x02, 0x12, 0xA7.toByte())),
    GetInterval(byteArrayOf(0x02, 0x12, 0xAB.toByte())),
    GetDuration(byteArrayOf(0x02, 0x12, 0xAC.toByte())),
    GetVideoPerformanceMode(byteArrayOf(0x02, 0x12, 0xAD.toByte())),
    GetControls(byteArrayOf(0x02, 0x12, 0xAF.toByte())),
    GetSpeed(byteArrayOf(0x02, 0x12, 0xB0.toByte())),
    GetNightPhoto(byteArrayOf(0x02, 0x12, 0xB1.toByte())),
    GetWirelessBand(byteArrayOf(0x02, 0x12, 0xB2.toByte())),
    GetTrailLength(byteArrayOf(0x02, 0x12, 0xB3.toByte())),
    GetVideoMode(byteArrayOf(0x02, 0x12, 0xB4.toByte())),
    GetVideoModeBitRate(byteArrayOf(0x02, 0x12, 0xB6.toByte())),
    GetVideoModeBitDepth(byteArrayOf(0x02, 0x12, 0xB7.toByte())),
    GetVideoModeProfiles(byteArrayOf(0x02, 0x12, 0xB8.toByte())),
    GetVideoModeAspectRatio(byteArrayOf(0x02, 0x12, 0xB9.toByte())),
    GetVideoModeGoPro12(byteArrayOf(0x02, 0x12, 0xBA.toByte())),
    GetLapseMode(byteArrayOf(0x02, 0x12, 0xBB.toByte())),
    GetLapseModeAspectRatio(byteArrayOf(0x02, 0x12, 0xBC.toByte())),
    GetLapseModeMaxLensMod(byteArrayOf(0x02, 0x12, 0xBD.toByte())),
    GetLapseModeMaxLensModEnabled(byteArrayOf(0x02, 0x12, 0xBE.toByte())),
    GetPhotoMode(byteArrayOf(0x02, 0x12, 0xBF.toByte())),
    GetPhotoModeAspectRatio(byteArrayOf(0x02, 0x12, 0xC0.toByte())),
    GetFraming(byteArrayOf(0x02, 0x12, 0xC1.toByte())),

    // QUERY STATUS
    GetCurrentMode(byteArrayOf(0x02, 0x13, 0x59)),
    GetActivePresetGroup(byteArrayOf(0x02, 0x13, 0x60)),
    GetActivePreset(byteArrayOf(0x02, 0x13, 0x61));

    companion object {
        fun valueOfByteArray(byteArray: ByteArray): GoProBleCommands? {
            for (command in values()) {
                if (command.byteArray.contentEquals(byteArray)) {
                    return command
                }
            }
            return null
        }
    }
}