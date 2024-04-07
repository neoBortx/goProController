package com.bortxapps.goprocontrollerandroid.infrastructure.ble

import android.util.Log
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.simplebleclient.api.contracts.BleNetworkMessageProcessor
import com.bortxapps.simplebleclient.api.data.BleNetworkMessage
import java.util.UUID
import kotlin.experimental.and

class FragmentedNetworkMessageProcessorImpl: BleNetworkMessageProcessor {

    private var packet = byteArrayOf()
    private var bytesRemaining = 0
    private var characteristic: UUID? = null

    companion object {
        private const val EXT_16_HEADER_SIZE = 3
        private const val EXT_13_HEADER_SIZE = 2
        private const val GENERAL_HEADER_SIZE = 1
        private const val BITS_8 = 8
        private const val BITS_5 = 5

    }

    private enum class Mask(val value: Byte) {
        Header(0b01100000),
        Continuation(0b10000000.toByte()),
        GenLength(0b00011111),
        Ext13Byte0(0b00011111)
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
    override fun processMessage(characteristic: UUID, data: ByteArray) {

        if (isWiffiMessage(characteristic)) {
            packet = data
            bytesRemaining = 0
        } else {
            if (isContinuationMessage(data)) {
                packet += data.drop(1).toByteArray()
                bytesRemaining -= data.size - 1
            } else {
                processNewMessage(characteristic, data)
            }
        }
    }

    private fun isWiffiMessage(characteristic: UUID)  = characteristic == GoProUUID.WIFI_AP_SSID.uuid
            || characteristic == GoProUUID.WIFI_AP_PASSWORD.uuid
            || characteristic == GoProUUID.WIFI_AP_SERVICE.uuid

    private fun processNewMessage(characteristic: UUID, data: ByteArray) {
        this.characteristic = characteristic
        packet = byteArrayOf()
        var buff = data
        when (Header.fromValue((data.first() and Mask.Header.value).toInt() shr BITS_5)) {
            Header.GENERAL -> {
                bytesRemaining = data[0].and(Mask.GenLength.value).toInt()
                buff = buff.drop(GENERAL_HEADER_SIZE).toByteArray()
            }

            Header.EXT_13 -> {
                bytesRemaining = ((data[0].and(Mask.Ext13Byte0.value).toLong() shl BITS_8) or data[1].toLong()).toInt()
                buff = buff.drop(EXT_13_HEADER_SIZE).toByteArray()
            }

            Header.EXT_16 -> {
                bytesRemaining = ((data[1].toLong() shl BITS_8) or data[2].toLong()).toInt()
                buff = buff.drop(EXT_16_HEADER_SIZE).toByteArray()
            }

            Header.RESERVED -> {
                throw NoSuchElementException("Reserved header")
            }
        }
        if (buff.size >= bytesRemaining){
            packet += buff.take(bytesRemaining)
        } else {
            packet += buff
        }

        if (buff.size >= bytesRemaining) bytesRemaining = 0 else bytesRemaining -= buff.size
    }

    override fun clearData() {
        packet = byteArrayOf()
        bytesRemaining = 0
        characteristic = null
    }

    private fun isContinuationMessage(data: ByteArray) = data.firstOrNull()?.and(Mask.Continuation.value) == Mask.Continuation.value

    override fun isFullyReceived(): Boolean {
        Log.e("BleNetworkMessageProcessor", "bytesRemaining in isFullyReceived -> $bytesRemaining")
        return bytesRemaining == 0
    }

    override fun getPacket(): BleNetworkMessage = BleNetworkMessage(characteristic, packet, !isFullyReceived())
}