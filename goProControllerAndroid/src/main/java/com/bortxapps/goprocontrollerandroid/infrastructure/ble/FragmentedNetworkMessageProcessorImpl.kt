package com.bortxapps.goprocontrollerandroid.infrastructure.ble

import android.util.Log
import com.bortxapps.goprocontrollerandroid.feature.commands.data.GoProUUID
import com.bortxapps.simplebleclient.api.contracts.BleNetworkMessageProcessor
import com.bortxapps.simplebleclient.api.data.BleNetworkMessage
import java.util.UUID

@OptIn(ExperimentalUnsignedTypes::class)
class FragmentedNetworkMessageProcessorImpl: BleNetworkMessageProcessor {

    private var packet = ubyteArrayOf()
    private var bytesRemaining = 0
    private var characteristic: UUID? = null

    companion object {
        private const val EXT_16_HEADER_SIZE = 3
        private const val EXT_13_HEADER_SIZE = 2
        private const val GENERAL_HEADER_SIZE = 1
        private const val BITS_8 = 8
        private const val BITS_5 = 5

    }

    private enum class Mask(val value: UByte) {
        Header(0b01100000U),
        Continuation(0b10000000U),
        GenLength(0b00011111U),
        Ext13Byte0(0b00011111U)
    }

    private enum class Header(val value: UByte) {
        GENERAL(0b00U),
        EXT_13(0b01U),
        EXT_16(0b10U),
        RESERVED(0b11U);

        companion object {
            private val valueMap: Map<UByte, Header> by lazy {
                Header.values().associateBy { it.value }
            }

            fun fromValue(value: Int) = valueMap.getValue(value.toUByte())
        }
    }
    override fun processMessage(characteristic: UUID, data: ByteArray) {

        if (isWiffiMessage(characteristic)) {
            packet = data.toUByteArray()
            bytesRemaining = 0
        } else {
            if (isContinuationMessage(data.toUByteArray())) {
                packet += data.drop(1).toByteArray().toUByteArray()
                bytesRemaining -= data.size - 1
            } else {
                processNewMessage(characteristic, data.toUByteArray())
            }
        }
    }

    private fun isWiffiMessage(characteristic: UUID)  = characteristic == GoProUUID.WIFI_AP_SSID.uuid
            || characteristic == GoProUUID.WIFI_AP_PASSWORD.uuid
            || characteristic == GoProUUID.WIFI_AP_SERVICE.uuid

    private fun processNewMessage(characteristic: UUID, data: UByteArray) {
        this.characteristic = characteristic
        packet = ubyteArrayOf()
        var buff = data
        when (Header.fromValue((data.first() and Mask.Header.value).toInt() shr BITS_5)) {
            Header.GENERAL -> {
                bytesRemaining = data[0].and(Mask.GenLength.value).toInt()
                buff = buff.drop(GENERAL_HEADER_SIZE).toUByteArray()
            }

            Header.EXT_13 -> {
                bytesRemaining = ((data[0].and(Mask.Ext13Byte0.value).toLong() shl BITS_8) or data[1].toLong()).toInt()
                buff = buff.drop(EXT_13_HEADER_SIZE).toUByteArray()
            }

            Header.EXT_16 -> {
                bytesRemaining = ((data[1].toLong() shl BITS_8) or data[2].toLong()).toInt()
                buff = buff.drop(EXT_16_HEADER_SIZE).toUByteArray()
            }

            Header.RESERVED -> {
                throw NoSuchElementException("Reserved header")
            }
        }
        packet += if (buff.size >= bytesRemaining) buff.take(bytesRemaining) else buff
        if (buff.size >= bytesRemaining) bytesRemaining = 0 else bytesRemaining -= buff.size
    }

    override fun clearData() {
        packet = ubyteArrayOf()
        bytesRemaining = 0
        characteristic = null
    }

    private fun isContinuationMessage(data: UByteArray) = data.firstOrNull()?.and(Mask.Continuation.value) == Mask.Continuation.value

    override fun isFullyReceived(): Boolean {
        Log.e("BleNetworkMessageProcessor", "bytesRemaining in isFullyReceived -> $bytesRemaining")
        return bytesRemaining == 0
    }

    override fun getPacket(): BleNetworkMessage = BleNetworkMessage(characteristic, packet.toByteArray(), !isFullyReceived())
}