package com.bortxapps.goprocontrollerandroid.feature.commands.customSerializers

import org.json.JSONObject


fun customCameraStateSerializer(json: String): Map<String, String> {
    JSONObject(json).let { jsonObject ->
        return mutableMapOf<String, String>().apply {
            jsonObject.keys().forEach { key ->
                this[key] = jsonObject.getString(key)
            }
        }
    }
}