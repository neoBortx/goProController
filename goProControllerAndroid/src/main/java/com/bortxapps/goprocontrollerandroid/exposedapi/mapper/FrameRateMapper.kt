package com.bortxapps.goprocontrollerandroid.exposedapi.mapper

import com.bortxapps.goprocontrollerandroid.domain.data.GoProFrameRate
import com.bortxapps.goprocontrollerandroid.feature.QUERY_PARAM_SETTING_FRAME_RATE

internal fun GoProFrameRate.toQueryResolution(): QUERY_PARAM_SETTING_FRAME_RATE = when (this) {
    GoProFrameRate._30 -> QUERY_PARAM_SETTING_FRAME_RATE.FRAME_RATE_30
    GoProFrameRate._60 -> QUERY_PARAM_SETTING_FRAME_RATE.FRAME_RATE_60
    GoProFrameRate._100 -> QUERY_PARAM_SETTING_FRAME_RATE.FRAME_RATE_100
    GoProFrameRate._120 -> QUERY_PARAM_SETTING_FRAME_RATE.FRAME_RATE_120
    GoProFrameRate._240 -> QUERY_PARAM_SETTING_FRAME_RATE.FRAME_RATE_240
}