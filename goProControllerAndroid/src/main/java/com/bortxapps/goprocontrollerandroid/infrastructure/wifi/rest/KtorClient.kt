package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest

import android.util.Log
import com.bortxapps.goprocontrollerandroid.domain.data.GoProError
import com.bortxapps.goprocontrollerandroid.domain.data.GoProException
import com.bortxapps.goprocontrollerandroid.feature.GOPRO_BASE_URL
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
                    queryParams.forEach { (key, value) -> encodedParameters.append(key, value) }
                }
                contentType(ContentType.Application.Json)
                setBody(body)
            }
        } catch (ex: SocketException) {
            Log.e("KtorClient", "getImage ${ex.message} ${ex.stackTraceToString()}")
            throw GoProException(GoProError.COMMUNICATION_FAILED)
        } catch (ex: Exception) {
            Log.e("KtorClient", "getImage ${ex.message} ${ex.stackTraceToString()}")
            throw GoProException(GoProError.OTHER)
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
        Log.e("KtorClient", "getImage ${ex.message} ${ex.stackTraceToString()}")
        throw GoProException(GoProError.COMMUNICATION_FAILED)
    } catch (ex: Exception) {
        Log.e("KtorClient", "getImage ${ex.message} ${ex.stackTraceToString()}")
        throw GoProException(GoProError.OTHER)
    }

    suspend fun getFile(
        path: String,
        contentType: ContentType
    ): HttpResponse = try {
        ktorHttpClient.get(GOPRO_BASE_URL+"videos/DCIM/") {
            url {
                appendPathSegments(path)
            }
            contentType(contentType)
        }
    } catch (ex: SocketException) {
        Log.e("KtorClient", "getfile $path type $contentType -> Error  ${ex.message} -> ${ex.stackTraceToString()}")
        throw GoProException(GoProError.COMMUNICATION_FAILED)
    } catch (ex: Exception) {
        Log.e("KtorClient", "getImage ${ex.message} ${ex.stackTraceToString()}")
        throw GoProException(GoProError.OTHER)
    }
}