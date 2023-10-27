package com.bortxapps.goprocontrollerandroid.feature.base

import android.util.Log
import arrow.core.Either
import com.bortxapps.goprocontrollerandroid.domain.GoProError
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.data.BleNetworkMessage
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

abstract class RepositoryBaseBle {

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend inline fun <MAPPED> launchReadRequest(
        noinline  request: suspend () -> BleNetworkMessage,
        noinline customMapper: ((BleNetworkMessage) -> MAPPED)
    ): Either<GoProError, MAPPED> {
        return try {
            val requestLog = request.javaClass.enclosingMethod?.name ?: "Unknown"
            Log.d("RepositoryBaseBle", "launchReadRequest request -> $requestLog")
            request().let { response ->
                Log.d("RepositoryBaseBle", "launchReadRequest response -> $requestLog : ${response.id}, ${response.status}, ${response.data.toByteArray().joinToString(separator = ":") { String.format("%02X", it) }}}")
                val mapped: MAPPED = mapResponse(response, customMapper)
                Log.d("RepositoryBaseBle", "launchReadRequest mapped -> $requestLog : ${mapped.toString()}")
                Either.Right(mapped)
            }
        } catch (ex: RepositoryException) {
            Either.Left(ex.goProError)
        }
    }

    suspend fun launchSimpleWriteRequest(
        request: suspend () -> BleNetworkMessage?,
        responseValidator: ((BleNetworkMessage) -> Boolean)? = null,
    ): Either<GoProError, Boolean> {
        return try {
            Log.d("RepositoryBaseBle", "launchSimpleWriteRequest request -> ${request.javaClass.enclosingMethod?.name ?: "Unknown"}")
           if (request()?.let { responseValidator?.invoke(it) } == true){
               Either.Right(true)
           } else {
               Either.Left(GoProError.SEND_COMMAND_FAILED)
           }
        } catch (ex: RepositoryException) {
            Either.Left(ex.goProError)
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    suspend fun <MAPPED> launchComplexWriteRequest(
        request: suspend () -> BleNetworkMessage?,
        customMapper: ((BleNetworkMessage) -> MAPPED)
    ): Either<GoProError, MAPPED> {
        return try {
            val requestLog = request.javaClass.enclosingMethod?.name ?: "Unknown"
            Log.d("RepositoryBaseBle", "launchComplexWriteRequest request -> $requestLog")
            request()?.let { response ->
                Log.d("RepositoryBaseBle", "launchComplexWriteRequest response -> $requestLog : ${response.id}, ${response.status}, ${response.data.toByteArray().joinToString(separator = ":") { String.format("%02X", it) }}")
                val mapped: MAPPED = mapResponse(response, customMapper)
                Log.d("RepositoryBaseBle", "launchComplexWriteRequest mapped -> $requestLog : ${mapped.toString()}")
                Either.Right(mapped)
            } ?: Either.Left(GoProError.SEND_COMMAND_FAILED)
        } catch (ex: RepositoryException) {
            Either.Left(ex.goProError)
        }
    }

    fun <MAPPED> mapResponse(response: BleNetworkMessage, mapper: ((BleNetworkMessage) -> MAPPED)): MAPPED {
        return try {
            mapper(response)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw RepositoryException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }
}
