package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest

import com.bortxapps.goprocontrollerandroid.domain.GoProError
import com.bortxapps.goprocontrollerandroid.feature.GOPRO_BASE_URL
import com.bortxapps.goprocontrollerandroid.feature.base.RepositoryException
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.appendPathSegments
import io.ktor.http.contentType
import java.io.Serializable
import java.net.SocketException

class KtorClient(private var ktorHttpClient: HttpClient = getKtorHttpClient()) {

    suspend fun getRawRequest(
        path: String,
        queryParams: Map<String, String> = emptyMap(),
        body: Serializable? = null
    ): HttpResponse =
        try {
            ktorHttpClient.get(GOPRO_BASE_URL) {
                url {
                    appendPathSegments(path)
                    queryParams.forEach { (key, value) -> parameters.append(key, value) }
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        } catch (ex: SocketException) {
            ex.printStackTrace()
            throw RepositoryException(GoProError.COMMUNICATION_FAILED)
        } catch (ex: Exception) {
            ex.printStackTrace()
            throw RepositoryException(GoProError.OTHER)
        }

    suspend fun sendRawPostRequest(
        path: String,
        queryParams: Map<String, String> = emptyMap(),
        body: Serializable? = null
    ): HttpResponse = try {
        ktorHttpClient.post(GOPRO_BASE_URL) {
            url() {
                appendPathSegments(path)
                queryParams.forEach { (key, value) -> parameters.append(key, value) }
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    } catch (ex: SocketException) {
        ex.printStackTrace()
        throw RepositoryException(GoProError.COMMUNICATION_FAILED)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw RepositoryException(GoProError.OTHER)
    }


    suspend fun getVideo(
        path: String
    ): HttpResponse = try {
        ktorHttpClient.get(GOPRO_BASE_URL) {
            url {
                appendPathSegments(path)
            }
            contentType(ContentType.Video.MPEG)
        }
    } catch (ex: SocketException) {
        ex.printStackTrace()
        throw RepositoryException(GoProError.COMMUNICATION_FAILED)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw RepositoryException(GoProError.OTHER)
    }

    suspend fun getImage(
        path: String
    ): HttpResponse = try {
        ktorHttpClient.get(GOPRO_BASE_URL) {
            url {
                appendPathSegments(path)
            }
            contentType(ContentType.Image.JPEG)
        }
    } catch (ex: SocketException) {
        ex.printStackTrace()
        throw RepositoryException(GoProError.COMMUNICATION_FAILED)
    } catch (ex: Exception) {
        ex.printStackTrace()
        throw RepositoryException(GoProError.OTHER)
    }

}