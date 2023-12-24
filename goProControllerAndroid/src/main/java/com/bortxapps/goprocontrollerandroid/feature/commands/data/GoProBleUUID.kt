package com.bortxapps.goprocontrollerandroid.feature.commands.data

import java.util.UUID

const val GOPRO_FEA6_UUID = "0000FEA6-0000-1000-8000-00805F9B34FB"
const val BLE_DESCRIPTION_BASE_UUID = "00002902-0000-1000-8000-00805F9B34FB"
const val GOPRO_BASE_UUID = "b5f9%s-aa8d-11e3-9046-0002a5d5c51b"
const val GOPRO_NAME_PREFIX = "GoPro"

enum class GoProUUID(val uuid: UUID) {
    SERVICE_UUID(UUID.fromString(GOPRO_FEA6_UUID)),
    WIFI_AP_SERVICE(UUID.fromString(GOPRO_BASE_UUID.format("0001"))),
    WIFI_AP_PASSWORD(UUID.fromString(GOPRO_BASE_UUID.format("0003"))),
    WIFI_AP_SSID(UUID.fromString(GOPRO_BASE_UUID.format("0002"))),
    CQ_COMMAND(UUID.fromString(GOPRO_BASE_UUID.format("0072"))),
    CQ_COMMAND_RSP(UUID.fromString(GOPRO_BASE_UUID.format("0073"))),
    CQ_SETTING(UUID.fromString(GOPRO_BASE_UUID.format("0074"))),
    CQ_SETTING_RSP(UUID.fromString(GOPRO_BASE_UUID.format("0075"))),
    CQ_QUERY(UUID.fromString(GOPRO_BASE_UUID.format("0076"))),
    CQ_QUERY_RSP(UUID.fromString(GOPRO_BASE_UUID.format("0077")));
}