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
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.FragmentedNetworkMessageProcessorImpl
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.manager.WifiManager
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.KtorClient
import com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest.getKtorHttpClient
import com.bortxapps.simplebleclient.api.SimpleBleClientBuilder


internal class LibraryContainer(context: Context) {

    //wifi
    private val wifiManager = WifiManager()
    private val httpClient = getKtorHttpClient()
    private val ktorHttpClient = KtorClient(httpClient)

    companion object {

        /**
         * Number of messages to store in incoming message buffer, if the buffer is full, the oldest message will be
         * removed
         * Default 1
         */
        const val MESSAGE_BUFFER_SIZE = 20

        /**
         * BLE operation Timeout. If the operation (read, write, subscription or connection) takes longer than this an
         * exception will be thrown
         * Default 7000 milliseconds
         */

        const val OPERATION_TIMEOUT_MILLIS = 60000L

        /**
         * The duration of the timeout for scanning BLE devices
         * Default 10000 milliseconds
         */

        const val SCAN_PERIOD_MILLIS = 30000L

        /**
         * The number of messages to store in the incoming message buffer to new consumer of the incoming message flow
         * Default 0
        **/
        const val MESSAGE_BUFFER_RETRIES = 0
    }

    /**
     * Depending on the protocol used by the device, the message received from the device can be fragmented or may
     * require a special treatment.
     *
     * BLE operations are asynchronous but you only can perform one in a time. When you're expecting a fragmented
     * messages, you need to wait for the last message to be received before sending the next operation.
     * Because this is not straightforward, this interface is used to
     * encapsulate the logic to handle this kind of messages in lower layers for you.
     *
     * You should implement your own and pass it to the SimpleBleClientBuilder.
     *
     * Default null
     */
    private val messageProcessor = FragmentedNetworkMessageProcessorImpl()

    private val simpleBleClient = SimpleBleClientBuilder()
        .setMessageBufferRetries(MESSAGE_BUFFER_RETRIES)
        .setMessageBufferSize(MESSAGE_BUFFER_SIZE)
        .setOperationTimeOutMillis(OPERATION_TIMEOUT_MILLIS)
        .setScanPeriodMillis(SCAN_PERIOD_MILLIS)
        .setMessageProcessor(messageProcessor)
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