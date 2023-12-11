package com.bortxapps.goprocontrollerandroid.exposedapi

import android.content.Context
import com.bortxapps.goprocontrollerandroid.di.LibraryContainer
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException

object GoProApi {
    private var instance: GoProController? = null

    fun initialize(context: Context) {
        if (instance == null) {
            val container = LibraryContainer(context)
            instance = GoProControllerImpl(
                container.goProCommandsImpl,
                container.goProMediaImpl,
                container.goProConnectorImpl
            )
        }
    }

    fun getInstance(): GoProController {
        return instance ?: throw GoProException(GoProError.INITIALIZE_CONTROLLER_BEFORE_USE)
    }
}