package com.bortxapps.goprocontrollerandroid.feature.base

import arrow.core.Either
import com.bortxapps.goprocontrollerandroid.domain.GoProError
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText

abstract class RepositoryBase {

    suspend inline fun <reified DTO, MAPPED> launchRequest(
        request: () -> HttpResponse,
        noinline customSerializer: ((String) -> DTO)? = null,
        noinline customMapper: ((DTO) -> MAPPED)? = null,
    ): Either<GoProError, MAPPED> {
        try {
            request().let { response ->
                return when (response.status.value) {
                    in 200..299 -> {
                        processSuccess(response, customSerializer, customMapper)
                    }

                    else -> Either.Left(GoProError.CAMERA_API_ERROR)
                }
            }
        } catch (ex: RepositoryException) {
            return Either.Left(ex.goProError)
        }
    }

    suspend inline fun <reified DTO, MAPPED> processSuccess(
        response: HttpResponse,
        noinline customSerializer: ((String) -> DTO)?,
        noinline mapper: ((DTO) -> MAPPED)?
    ): Either.Right<MAPPED> {
        val serialized: DTO = serializeResponse(response, customSerializer)
        return Either.Right(mapResponse(serialized, mapper))
    }

    suspend inline fun <reified DTO> serializeResponse(response: HttpResponse, noinline customSerializer: ((String) -> DTO)? = null): DTO {
        return try {
            if (customSerializer != null) {
                customSerializer(response.bodyAsText())
            } else {
                response.body()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw RepositoryException(GoProError.UNABLE_TO_MAP_DATA)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <DTO, MAPPED> mapResponse(response: DTO, mapper: ((DTO) -> MAPPED)? = null): MAPPED {
        return if (mapper != null) {
            try {
                mapper(response)
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw RepositoryException(GoProError.UNABLE_TO_MAP_DATA)
            }
        } else {
            try {
                response as MAPPED
            } catch (ex: Exception) {
                ex.printStackTrace()
                throw RepositoryException(GoProError.MISSING_DTO_MAPPER)
            }
        }
    }
}
