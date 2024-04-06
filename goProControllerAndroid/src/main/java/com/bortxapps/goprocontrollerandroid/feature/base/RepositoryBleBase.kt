package com.bortxapps.goprocontrollerandroid.feature.base

import android.content.Context
import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.simplebleclient.api.data.BleNetworkMessage


abstract class RepositoryBleBase(val context: Context) {

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun <MAPPED> launchReadRequest(
        request: suspend () -> BleNetworkMessage,
        customMapper: ((BleNetworkMessage) -> MAPPED)
    ): Result<MAPPED> {
        val requestLog = request.javaClass.enclosingMethod?.name ?: "Unknown"
        request().let { response ->
            val mapped: MAPPED = mapResponse(response, customMapper)
            Log.d("RepositoryBaseBle", "launchReadRequest mapped -> $requestLog : $mapped")
            return Result.success(mapped)
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun launchSimpleWriteRequest(
        request: suspend () -> BleNetworkMessage,
        responseValidator: ((BleNetworkMessage) -> Boolean)? = null
    ): Result<Boolean> {
        request().let { response ->
            return if (responseValidator?.invoke(response) == true) {
                Result.success(true)
            } else {
                Log.e("RepositoryBaseBle", "Write operation rejected because ${response.data.last()}")
                Result.failure(GoProException(GoProError.SEND_COMMAND_REJECTED))
            }
        }
    }

    suspend fun <MAPPED> launchComplexWriteRequest(
        request: suspend () -> BleNetworkMessage?,
        customMapper: ((BleNetworkMessage) -> MAPPED)
    ): Result<MAPPED> {
        val requestLog = request.javaClass.enclosingMethod?.name ?: "Unknown"
        return request()?.let { response ->
            return try {
                val mapped: MAPPED = mapResponse(response, customMapper)
                Log.d("RepositoryBaseBle", "launchComplexWriteRequest mapped -> $requestLog : $mapped")
                Result.success(mapped)
            } catch (ex: GoProException) {
                Result.failure(ex)
            }

        } ?: Result.failure(GoProException(GoProError.SEND_COMMAND_REJECTED))

    }

    private fun <MAPPED> mapResponse(response: BleNetworkMessage, mapper: ((BleNetworkMessage) -> MAPPED)): MAPPED {
        return try {
            mapper(response)
        } catch (ex: GoProException) {
            Log.e("RepositoryBaseBle", "mapResponse ${ex.message} ${ex.stackTraceToString()}")
            throw ex
        } catch (ex: Exception) {
            Log.e("RepositoryBaseBle", "mapResponse ${ex.message} ${ex.stackTraceToString()}")
            throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    protected fun validateSimpleWriteResponse(response: BleNetworkMessage) = response.data.last() == 0x00.toByte()
}
