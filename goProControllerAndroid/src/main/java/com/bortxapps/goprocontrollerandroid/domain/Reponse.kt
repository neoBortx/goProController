package com.bortxapps.goprocontrollerandroid.domain

import arrow.core.Either

data class GoProResponse<T>(val statusCode: Int, val response: T)

typealias GoProResult<S> = Either<GoProError, GoProResponse<S>>