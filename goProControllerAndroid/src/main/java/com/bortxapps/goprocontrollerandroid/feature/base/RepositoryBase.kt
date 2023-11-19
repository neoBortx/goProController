package com.bortxapps.goprocontrollerandroid.feature.base

import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.client.statement.bodyAsText
import io.ktor.utils.io.jvm.javaio.toInputStream
import java.io.InputStream

abstract class RepositoryBase {

    companion object {
        const val REST_SUCCESS_CODE = 200
        const val REST_SUCCESS_FINISH_CODE = 299
    }

    suspend inline fun <reified DTO, MAPPED> launchRequest(
        request: () -> HttpResponse,
        noinline customSerializer: ((String) -> DTO)? = null,
        noinline customMapper: ((DTO) -> MAPPED)? = null
    ): Result<MAPPED> {
        return try {
            request().let { response ->
                return when (response.status.value) {
                    in REST_SUCCESS_CODE..REST_SUCCESS_FINISH_CODE -> {
                        processSuccess(response, customSerializer, customMapper)
                    }

                    else -> Result.failure(GoProException(GoProError.CAMERA_API_ERROR))
                }
            }
        } catch (ex: GoProException) {
             Result.failure(ex)
        } catch (ex: Exception) {
            Log.e("RepositoryBase", "launchRequest ${ex.message} ${ex.stackTraceToString()}")
            Result.failure(GoProException(GoProError.CAMERA_API_ERROR))
        }
    }

    suspend inline fun launchFileRequest(request: () -> HttpResponse): Result<InputStream> {
        return try {
            request().let { response ->
                return when (response.status.value) {
                    in REST_SUCCESS_CODE..REST_SUCCESS_FINISH_CODE -> {
                        return Result.success(response.bodyAsChannel().toInputStream())
                    }

                    else -> Result.failure(GoProException(GoProError.CAMERA_API_ERROR))
                }
            }
        } catch (ex: GoProException) {
            Result.failure(ex)
        } catch (ex: Exception) {
            Log.e("RepositoryBase", "launchRequest ${ex.message} ${ex.stackTraceToString()}")
            Result.failure(GoProException(GoProError.CAMERA_API_ERROR))
        }
    }

    suspend inline fun <reified DTO> launchSimpleRequest(
        request: () -> HttpResponse,
    ): DTO {
        try {
            request().let { response ->
                return when (response.status.value) {
                    in REST_SUCCESS_CODE..REST_SUCCESS_FINISH_CODE -> {
                        serializeResponse(response)
                    }
                    else ->throw GoProException(GoProError.CAMERA_API_ERROR)
                }
            }
        } catch (ex: GoProException) {
            throw ex
        } catch (ex: Exception) {
            Log.e("RepositoryBase", "launchRequest ${ex.message} ${ex.stackTraceToString()}")
            throw GoProException(GoProError.CAMERA_API_ERROR)
        }
    }

    suspend inline fun <reified DTO, MAPPED> processSuccess(
        response: HttpResponse,
        noinline customSerializer: ((String) -> DTO)?,
        noinline mapper: ((DTO) -> MAPPED)?
    ): Result<MAPPED> {
        val serialized: DTO = serializeResponse(response, customSerializer)
        return Result.success(mapResponse(serialized, mapper))
    }

    suspend inline fun <reified DTO> serializeResponse(
        response: HttpResponse, noinline customSerializer:
        ((String) -> DTO)? = null
    ): DTO {
        return try {
            if (customSerializer != null) {
                customSerializer(response.bodyAsText())
            } else {
                response.body()
            }
        } catch (ex: Exception) {
            Log.e("RepositoryBase", "serializeResponse ${ex.message} ${ex.stackTraceToString()}")
            throw GoProException(GoProError.UNKNOWN_RECEIVED_DATA)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <DTO, MAPPED> mapResponse(response: DTO, mapper: ((DTO) -> MAPPED)? = null): MAPPED {
        return if (mapper != null) {
            try {
                mapper(response)
            } catch (ex: Exception) {
                Log.e("RepositoryBase", "mapResponse ${ex.message} ${ex.stackTraceToString()}")
                throw GoProException(GoProError.UNABLE_TO_MAP_DATA)
            }
        } else {
            try {
                response as MAPPED
            } catch (ex: Exception) {
                Log.e("RepositoryBase", "mapResponse ${ex.message} ${ex.stackTraceToString()}")
                throw GoProException(GoProError.MISSING_DTO_MAPPER)
            }
        }
    }
}