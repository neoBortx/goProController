package com.bortxapps.goprocontrollerandroid.infrastructure.ble.data

@OptIn(ExperimentalUnsignedTypes::class)
data class BleNetworkMessage(val id: Int, val status: Int, val data: UByteArray)