package com.bortxapps.goprocontrollerandroid.exposedapi.mapper

import com.bortxapps.goprocontrollerandroid.domain.data.GoProResolution
import com.bortxapps.goprocontrollerandroid.feature.QUER_PARAM_SETTINGS_RESOLUTION

internal fun GoProResolution.toGoProResolution(): QUER_PARAM_SETTINGS_RESOLUTION = when (this) {
    GoProResolution._1080 -> QUER_PARAM_SETTINGS_RESOLUTION.RESOLUTION_1080
    GoProResolution._2_7K -> QUER_PARAM_SETTINGS_RESOLUTION.RESOLUTION_2_7K
    GoProResolution._4K -> QUER_PARAM_SETTINGS_RESOLUTION.RESOLUTION_4K
}