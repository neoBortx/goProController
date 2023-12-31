package com.bortxapps.goprocontrollerandroid.domain.data

import java.lang.Exception

class GoProException(val goProError: GoProError) : Exception() {
    override val message: String
        get() = goProError.toString()
}