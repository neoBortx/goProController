package com.bortxapps.goprocontrollerandroid.infrastructure.ble.data

import android.util.Log
import kotlin.experimental.and

@OptIn(ExperimentalUnsignedTypes::class)
class BleNetworkMessageProcessor {

    private var packet = ubyteArrayOf()
    private var bytesRemaining = 0

    private enum class Mask(val value: UByte) {
        Header(0b01100000U),
        Continuation(0b10000000U),
        GenLength(0b00011111U),
        Ext13Byte0(0b00011111U)
    }

    private enum class Header(val value: Byte) {
        GENERAL(0b00),
        EXT_13(0b01),
        EXT_16(0b10),
        RESERVED(0b11);

        companion object {
            private val valueMap: Map<Byte, Header> by lazy {
                Header.values().associateBy { it.value }
            }

            fun fromValue(value: Int) = valueMap.getValue(value.toByte())
        }
    }

    fun processSimpleMessage(data: UByteArray) {
        packet = data
        bytesRemaining = 0
    }

    fun processMessage(data: UByteArray) {
        if (isContinuationMessage(data)) {
            packet += data.drop(1).toUByteArray()
            bytesRemaining -= data.size - 1
        } else {
            processNewMessage(data)
        }
        Log.e("BleNetworkMessageProcessor", "Precessed data, remaining bytes -> $bytesRemaining")
    }

    private fun processNewMessage(data: UByteArray) {
        packet = ubyteArrayOf()
        var buff = data
        when (Header.fromValue((data.first() and Mask.Header.value).toInt() shr 5)) {
            Header.GENERAL -> {
                bytesRemaining = data[0].and(Mask.GenLength.value).toInt()
                buff = buff.drop(1).toUByteArray()
            }

            Header.EXT_13 -> {
                bytesRemaining = ((data[0].and(Mask.Ext13Byte0.value).toLong() shl 8) or data[1].toLong()).toInt()
                buff = buff.drop(2).toUByteArray()
            }

            Header.EXT_16 -> {
                bytesRemaining = ((data[1].toLong() shl 8) or data[2].toLong()).toInt()
                buff = buff.drop(3).toUByteArray()
            }

            Header.RESERVED -> {
                throw Exception("Unexpected RESERVED header")
            }
        }
        packet += buff
        bytesRemaining -= buff.size
    }

    private fun isContinuationMessage(data: UByteArray) = data.first().and(Mask.Continuation.value) == Mask.Continuation.value

    fun isReceived() = bytesRemaining == 0

    fun getPacket() = BleNetworkMessage(id(), status(), packet)

    private fun id() = packet[0].toInt()
    private fun status() = packet[1].toInt()
}