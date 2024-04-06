package com.bortxapps.goprocontrollerandroid.feature.base.mappers

import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.simplebleclient.exceptions.BleError
import com.bortxapps.simplebleclient.exceptions.SimpleBleClientException

fun mapBleException(e: SimpleBleClientException): GoProException {
    return when (e.bleError) {
        BleError.BLE_DEVICE_NOT_FOUND -> GoProException(GoProError.CAMERA_NOT_FOUND)
        BleError.BLE_DEVICE_NOT_CONNECTED -> GoProException(GoProError.CAMERA_NOT_CONNECTED)
        BleError.MISSING_BLE_PERMISSIONS -> GoProException(GoProError.MISSING_BLE_PERMISSIONS)
        BleError.BLE_NOT_ENABLED -> GoProException(GoProError.BLE_NOT_ENABLED)
        BleError.BLE_NOT_SUPPORTED -> GoProException(GoProError.BLE_NOT_SUPPORTED)
        BleError.COMMUNICATION_FAILED -> GoProException(GoProError.COMMUNICATION_FAILED)
        BleError.SEND_COMMAND_FAILED -> GoProException(GoProError.SEND_COMMAND_FAILED)
        BleError.UNABLE_TO_SUBSCRIBE_TO_NOTIFICATIONS -> GoProException(GoProError.UNABLE_TO_SUBSCRIBE_TO_NOTIFICATIONS)
        BleError.CANNOT_START_SEARCHING_DEVICES -> GoProException(GoProError.CANNOT_START_SEARCHING_CAMERAS)
        BleError.ALREADY_SEARCHING_BLE_DEVICES -> GoProException(GoProError.ALREADY_SEARCHING_CAMERAS)
        BleError.UNABLE_INITIALIZE_CONTROLLER -> GoProException(GoProError.UNABLE_INITIALIZE_CONTROLLER)
        BleError.BLE_DEVICE_NOT_RESPONDING -> GoProException(GoProError.CAMERA_NOT_RESPONDING)
        BleError.LIBRARY_NOT_INITIALIZED -> GoProException(GoProError.INTERNAL_ERROR)
        BleError.INTERNAL_ERROR -> GoProException(GoProError.INTERNAL_ERROR)
        else -> GoProException(GoProError.OTHER)
    }
}