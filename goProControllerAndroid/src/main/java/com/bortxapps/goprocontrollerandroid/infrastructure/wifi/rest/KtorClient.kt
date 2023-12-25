package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest

import android.util.Log
import com.bortxapps.goprocontrollerandroid.feature.GOPRO_BASE_URL
import com.bortxapps.goprocontrollerandroid.feature.GOPRO_MEDIA_PATH
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.BleError
import com.bortxapps.goprocontrollerandroid.infrastructure.ble.exceptions.SimpleBleClientException
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

class KtorClient(private var httpClient: HttpClient) {

    suspend fun getRawRequest(
        path: String,
        queryParams: Map<String, String> = emptyMap(),
        body: Serializable? = null
    ): HttpResponse = executeRequest {
        httpClient.get(GOPRO_BASE_URL) {
            url {
                appendPathSegments(path)
                queryParams.forEach { (key, value) -> encodedParameters.append(key, value) }
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    suspend fun sendRawPostRequest(
        path: String,
        queryParams: Map<String, String> = emptyMap(),
        body: Serializable? = null
    ): HttpResponse = executeRequest {
        httpClient.post(GOPRO_BASE_URL) {
            url {
                appendPathSegments(path)
                queryParams.forEach { (key, value) -> parameters.append(key, value) }
            }
            contentType(ContentType.Application.Json)
            setBody(body)
        }
    }

    suspend fun getFile(
        path: String,
        contentType: ContentType
    ): HttpResponse = executeRequest {
        httpClient.get(GOPRO_MEDIA_PATH) {
            url {
                appendPathSegments(path)
            }
            contentType(contentType)
        }
    }

    private inline fun <T> executeRequest(
        action: () -> T,
    ): T {
        try {
            return action()
        } catch (ex: SocketException) {
            Log.e("KtorClient", "Error: ${ex.message} ${ex.stackTraceToString()}")
            throw SimpleBleClientException(BleError.COMMUNICATION_FAILED)
        } catch (ex: Exception) {
            Log.e("KtorClient", "Error: ${ex.message} ${ex.stackTraceToString()}")
            throw SimpleBleClientException(BleError.OTHER)
        }
    }
}
