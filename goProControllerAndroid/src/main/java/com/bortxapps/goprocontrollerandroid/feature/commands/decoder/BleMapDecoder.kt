package com.bortxapps.goprocontrollerandroid.feature.commands.decoder

import android.util.Log
import com.bortxapps.simplebleclient.data.BleNetworkMessage
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.util.Locale

@OptIn(ExperimentalUnsignedTypes::class)
internal fun decodeMessageAsMap(bleNetworkMessage: BleNetworkMessage): Map<UByte, UByteArray> {
    Log.d(
        "BleMapDecoder",
        "Decoding message: ${
            bleNetworkMessage.data.toByteArray().joinToString(separator = ":") {
                String.format(Locale.getDefault(), "%02X", it)
            }
        }"
    )
    var buf = bleNetworkMessage.data.drop(2)
    val map = mutableMapOf<UByte, UByteArray>()
    while (buf.isNotEmpty()) {
        val paramId = buf[0]
        val paramLen = buf[1].toInt()
        buf = buf.drop(2)

        val paramVal = buf.take(paramLen)
        map[paramId] = paramVal.toUByteArray()
        buf = buf.drop(paramLen)
    }

    Log.d("BleMapDecoder", "Decoded message: ${prettyJson.encodeToString(map.toJsonElement())}")
    return map
}

val prettyJson by lazy { Json { prettyPrint = true } }

fun Array<*>.toJsonArray(): JsonArray {
    val array = mutableListOf<JsonElement>()
    this.forEach { array.add(it.toJsonElement()) }
    return JsonArray(array)
}

fun List<*>.toJsonArray(): JsonArray {
    val array = mutableListOf<JsonElement>()
    this.forEach { array.add(it.toJsonElement()) }
    return JsonArray(array)
}

fun Map<*, *>.toJsonObject(): JsonObject {
    val map = mutableMapOf<String, JsonElement>()
    this.forEach {
        val keyStr = it.key.toString()
        require(map.containsKey(keyStr)) {
            "Encoding duplicate keys $keyStr"
        }
        map[keyStr] = it.value.toJsonElement()
    }
    return JsonObject(map)
}

@OptIn(ExperimentalUnsignedTypes::class)
fun Any?.toJsonElement(): JsonElement {
    return when (this) {
        is Number -> JsonPrimitive(this)
        is Boolean -> JsonPrimitive(this)
        is String -> JsonPrimitive(this)
        is Array<*> -> this.toJsonArray()
        is List<*> -> this.toJsonArray()
        is Map<*, *> -> this.toJsonObject()
        is UByteArray -> this.toByteArray().joinToString(separator = ":") {
            String.format(Locale.getDefault(), "%02X", it)
        }.toJsonElement()

        is UByte -> ubyteArrayOf(this).toJsonElement()
        is JsonElement -> this
        null -> JsonNull
        else -> {
            throw IllegalArgumentException("Can not encode value ${this::class} to JSON")
        }
    }
}