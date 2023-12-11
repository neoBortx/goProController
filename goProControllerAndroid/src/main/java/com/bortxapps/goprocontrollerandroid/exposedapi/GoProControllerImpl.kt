package com.bortxapps.goprocontrollerandroid.exposedapi

import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProCommands
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProConnector
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProMedia
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException

internal class GoProControllerImpl(
    private val commands: GoProCommands,
    private val media: GoProMedia,
    private val connector: GoProConnector
) : GoProController, GoProMedia by media, GoProCommands by commands, GoProConnector by connector {

    override suspend fun connectToDevice(address: String): Result<Boolean> {
        return connector.connectToDevice(address).fold({
            getWifiApSSID().map { true }
        }, {
            Result.failure(GoProException(GoProError.OTHER))
        })
    }
}