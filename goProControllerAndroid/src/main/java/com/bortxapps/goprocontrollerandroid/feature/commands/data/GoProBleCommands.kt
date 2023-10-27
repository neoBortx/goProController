package com.bortxapps.goprocontrollerandroid.feature.commands.data


enum class GoProBleCommands(val byteArray: ByteArray) {
    EnableWifiCommand(byteArrayOf(0x03, 0x17, 0x01, 0x01)),
    DisableWifiCommand(byteArrayOf(0x03, 0x17, 0x01, 0x00)),
    GetHardwareInfoCommand(byteArrayOf(0x01, 0x3C)),
    GetOpenGoProVersionCommand(byteArrayOf(0x01, 0x51)),
    GetCameraSettings(byteArrayOf(0x01, 0x12)),
    GetCameraStatus(byteArrayOf(0x01, 0x13)),
    SetPresetsVideoCommand(byteArrayOf(0x04, 0x3E, 0x02, 0x03, 0xE8.toByte())),
    SetPresetsPhotoCommand(byteArrayOf(0x04, 0x3E, 0x02, 0x03, 0xE9.toByte())),
    SetPresetsTimeLapseCommand(byteArrayOf(0x04, 0x3E, 0x02, 0x03, 0xEA.toByte()));
}