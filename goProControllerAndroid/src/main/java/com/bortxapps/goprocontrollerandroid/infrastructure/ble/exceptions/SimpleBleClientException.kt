package com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions

class SimpleBleClientException(val bleError: BleError) : Exception() {
    override val message: String
        get() = bleError.toString()
}