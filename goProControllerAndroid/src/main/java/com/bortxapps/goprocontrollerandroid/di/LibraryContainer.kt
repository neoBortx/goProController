package com.bortxapps.goprocontrollerandroid.di

import android.content.Context
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProCommands
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProConnector
import com.bortxapps.goprocontrollerandroid.domain.contracts.GoProMedia
import com.bortxapps.goprocontrollerandroid.feature.commands.GoProCommandsImpl
import com.bortxapps.goprocontrollerandroid.feature.commands.api.CommandsApi
import com.bortxapps.goprocontrollerandroid.feature.connection.GoProConnectorImpl
import com.bortxapps.goprocontrollerandroid.feature.connection.api.ConnectionApi
import com.bortxapps.goprocontrollerandroid.feature.media.GoProMediaImpl
import com.bortxapps.goprocontrollerandroid.feature.media.api.MediaApi
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.api.SimpleBleClientBuilder
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiManager
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.KtorClient
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.getKtorHttpClient

internal class LibraryContainer(context: Context) {

    //wifi
    private val wifiManager = WifiManager()
    private val httpClient = getKtorHttpClient()
    private val ktorHttpClient = KtorClient(httpClient)


    private val simpleBleClient = SimpleBleClientBuilder
        .setOperationTimeOutMillis(7000)
        .build(context)

    //features api
    private val mediaApi = MediaApi(ktorHttpClient)
    private val connectionApi = ConnectionApi(simpleBleClient)
    private val commandsApi = CommandsApi(simpleBleClient)

    //features repository
    val goProMediaImpl: GoProMedia = GoProMediaImpl(context.applicationContext, mediaApi, wifiManager)
    val goProConnectorImpl: GoProConnector = GoProConnectorImpl(context.applicationContext, connectionApi)
    val goProCommandsImpl: GoProCommands = GoProCommandsImpl(context.applicationContext, commandsApi)
}