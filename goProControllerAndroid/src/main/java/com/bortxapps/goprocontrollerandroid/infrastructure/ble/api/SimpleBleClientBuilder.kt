package com.bortxapps.goprocontrollerandroid.infrastructure.ble.api

import android.content.Context
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.di.BleLibraryContainer
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.BleManager
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.contracts.SimpleBleClient

object SimpleBleClientBuilder {
    private var operationTimeOutMillis: Long? = null
    fun setOperationTimeOutMillis(timeout: Long) = apply { this.operationTimeOutMillis = timeout }
    fun build(context: Context): SimpleBleClient {
        return buildInstance(BleLibraryContainer(context))
    }

    private fun buildInstance(container: BleLibraryContainer): SimpleBleClient {
        operationTimeOutMillis?.let { container.bleConfiguration.operationTimeoutMillis = it }
        return BleManager(
            container.bleManagerDeviceSearchOperations,
            container.bleManagerGattConnectionOperations,
            container.bleManagerGattSubscriptions,
            container.bleManagerGattReadOperations,
            container.bleManagerGattWriteOperations,
            container.bleManagerGattCallBacks,
        )
    }
}