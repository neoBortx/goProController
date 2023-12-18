package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils

import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout
import java.util.concurrent.CancellationException

internal abstract class BleManagerGattOperationBase(private val gattMutex: Mutex) {

    companion object {
        private const val OPERATION_TIME_OUT: Long = 7000
    }

    protected suspend fun <T> launchGattOperation(operation: suspend () -> T): T {

        val error: GoProError = try {
            return  withTimeout(OPERATION_TIME_OUT) {
                gattMutex.withLock {
                    operation()
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e("BleManager", "launchGattOperation TIMEOUT ${e.message} ${e.stackTraceToString()}")
            GoProError.CAMERA_NOT_RESPONDING
        } catch (e: GoProException) {
            e.goProError
        } catch (e: Exception) {
            Log.e("BleManager", "launchGattOperation ERROR ${e.message} ${e.stackTraceToString()}")
            GoProError.COMMUNICATION_FAILED
        }

        throw GoProException(error)
    }

    protected suspend fun <T> launchDeferredOperation(operation: suspend () -> T): T {

        val error: GoProError = try {
            return operation()
        } catch (e: CancellationException) {
            Log.e("BleManager", "launchGattOperation Failed ${e.message} ${e.stackTraceToString()}")
            GoProError.COMMUNICATION_FAILED
        } catch (e: UninitializedPropertyAccessException) {
            Log.e("BleManager", "launchGattOperation Failed ${e.message} ${e.stackTraceToString()}")
            GoProError.INTERNAL_ERROR
        } catch (e: Exception) {
            Log.e("BleManager", "launchGattOperation Failed ${e.message} ${e.stackTraceToString()}")
            GoProError.OTHER
        }

        throw GoProException(error)
    }
}