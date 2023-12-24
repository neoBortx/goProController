package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager

internal class BleConfiguration {
    companion object {
        private const val OPERATION_TIME_OUT: Long = 7000
    }

    var operationTimeoutMillis: Long = OPERATION_TIME_OUT
}