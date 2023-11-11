package com.bortxapps.goprocontrollerandroid.domain.data

enum class GoProError {
    NO_GO_PRO_FOUND,
    CAMERA_NOT_CONNECTED,

    /*
     * This error is thrown when the device does not have the necessary permissions to use BLE
     */
    MISSING_BLE_PERMISSIONS,

    /*
     * This error is thrown when the device does not have Bluetooth enabled
     */
    BLE_NOT_ENABLED,

    /*
     * This error is thrown when the device hardware does not support BLE
     */
    BLE_NOT_SUPPORTED,
    COMMUNICATION_FAILED,
    SEND_COMMAND_FAILED,
    SEND_COMMAND_REJECTED,
    UNABLE_TO_MAP_DATA,
    MISSING_DTO_MAPPER,
    OTHER,
    CAMERA_API_ERROR,
    CANNOT_START_SEARCHING_CAMERAS
}