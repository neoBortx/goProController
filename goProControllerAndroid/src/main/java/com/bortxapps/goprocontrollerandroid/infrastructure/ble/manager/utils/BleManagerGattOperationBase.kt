package com.bortxapps.goprocontrollerandroid.infrastructure.ble.manager.utils

import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withTimeout

internal abstract class BleManagerGattOperationBase(private val gattMutex: Mutex) {

    companion object {
        private const val OPERATION_TIME_OUT: Long = 7000
    }

    protected suspend fun <T> launchGattOperation(operation: suspend () -> T): T {
        return try {
            withTimeout(OPERATION_TIME_OUT) {
                gattMutex.withLock {
                    operation()
                }
            }
        } catch (e: TimeoutCancellationException) {
            Log.e("BleManager", "launchGattOperation TIMEOUT ${e.message} ${e.stackTraceToString()}")
            throw GoProException(GoProError.CAMERA_NOT_RESPONDING)
        } catch (e: GoProException) {
            throw e
        } catch (e: Exception) {
            Log.e("BleManager", "launchGattOperation ERROR ${e.message} ${e.stackTraceToString()}")
            throw GoProException(GoProError.COMMUNICATION_FAILED)
        }
    }
}