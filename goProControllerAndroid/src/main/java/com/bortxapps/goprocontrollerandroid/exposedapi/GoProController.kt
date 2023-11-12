package com.bortxapps.goprocontrollerandroid.exposedapi

import android.content.Context
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProCommands
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProConnector
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProMedia
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.commands.GoProCommandsImpl
import com.bortxapps.goprocontrollerandroid.feature.connection.GoProConnectorImpl
import com.bortxapps.goprocontrollerandroid.feature.media.GoProMediaImpl

class GoProController(
    private val context: Context,
    private val commands: GoProCommands = GoProCommandsImpl(context),
    private val media: GoProMedia = GoProMediaImpl(context),
    private val connector: GoProConnector = GoProConnectorImpl(context)
) : GoProMedia by media, GoProCommands by commands, GoProConnector by connector {

    override suspend fun connectToDevice(address: String): Result<Boolean> {
        return connector.connectToDevice(address).fold({
            getWifiApSSID().map { true }
        }, {
            Result.failure(GoProException(GoProError.OTHER))
        })
    }
}