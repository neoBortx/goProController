package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest

import com.bortxapps.goprocontrollerandroid.utils.mockHttp.createHttpMockClient
import com.bortxapps.goprocontrollerandroid.utils.mockHttp.createMockHttpClientException
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.net.SocketException

class KtorClientTest {

    private lateinit var ktorClient: KtorClient

    private val body = "{ item: value}"

    @Test
    fun `getRawRequest should handle an exception`(): Unit = runBlocking {
        ktorClient = KtorClient(createMockHttpClientException(Exception()))

        try {
            ktorClient.getRawRequest("/path")
        } catch (e: Exception) {
            assertEquals("com.bortxapps.goprocontrollerandroid.domain.data.GoProException: OTHER", e.toString())
        }
    }

    @Test
    fun `getRawRequest should handle a SocketException`(): Unit = runBlocking {
        ktorClient = KtorClient(createMockHttpClientException(SocketException()))
        try {
            ktorClient.getRawRequest("/path")
        } catch (e: Exception) {
            assertEquals("com.bortxapps.goprocontrollerandroid.domain.data.GoProException: COMMUNICATION_FAILED", e.toString())
        }
    }

    @Test
    fun `getRawRequest should handle success`() = runBlocking {
        ktorClient = KtorClient(createHttpMockClient(HttpStatusCode.OK, body))
        val response = ktorClient.getRawRequest("/path")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(body, response.bodyAsText())
    }

    @Test
    fun `getRawRequest with list of parameters for the query should add parameters to the query`() = runBlocking {
        ktorClient = KtorClient(createHttpMockClient(HttpStatusCode.OK, body))
        val response = ktorClient.getRawRequest("/path", mapOf("param1" to "value1", "param2" to "value2"))
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("value1", response.request.url.parameters["param1"])
        assertEquals("value2", response.request.url.parameters["param2"])
        assertEquals(body, response.bodyAsText())
    }

    @Test
    fun `sendRawPostRequest should handle an exception`(): Unit = runBlocking {
        ktorClient = KtorClient(createMockHttpClientException(Exception()))

        try {
            ktorClient.sendRawPostRequest("/path", body = body)
        } catch (e: Exception) {
            assertEquals("com.bortxapps.goprocontrollerandroid.domain.data.GoProException: OTHER", e.toString())
        }
    }

    @Test
    fun `sendRawPostRequest should handle a SocketException`(): Unit = runBlocking {
        ktorClient = KtorClient(createMockHttpClientException(SocketException()))
        try {
            ktorClient.sendRawPostRequest("/path")
        } catch (e: Exception) {
            assertEquals("com.bortxapps.goprocontrollerandroid.domain.data.GoProException: COMMUNICATION_FAILED", e.toString())
        }
    }

    @Test
    fun `sendRawPostRequest should handle success`() = runBlocking {
        ktorClient = KtorClient(createHttpMockClient(HttpStatusCode.OK, body))
        val response = ktorClient.sendRawPostRequest("/path", body = body)
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(body, response.bodyAsText())
    }

    @Test
    fun `sendRawPostRequest with list of parameters for the query should add parameters to the query`() = runBlocking {
        ktorClient = KtorClient(createHttpMockClient(HttpStatusCode.OK, body))
        val response = ktorClient.sendRawPostRequest("/path", mapOf("param1" to "value1", "param2" to "value2"))
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("value1", response.request.url.parameters["param1"])
        assertEquals("value2", response.request.url.parameters["param2"])
        assertEquals(body, response.bodyAsText())
    }

    @Test
    fun `getFile should handle an exception`(): Unit = runBlocking {
        ktorClient = KtorClient(createMockHttpClientException(Exception()))

        try {
            ktorClient.getFile("/path", io.ktor.http.ContentType.Application.Json)
        } catch (e: Exception) {
            assertEquals("com.bortxapps.goprocontrollerandroid.domain.data.GoProException: OTHER", e.toString())
        }
    }

    @Test
    fun `getFile should handle a SocketException`(): Unit = runBlocking {
        ktorClient = KtorClient(createMockHttpClientException(SocketException()))
        try {
            ktorClient.getFile("/path", io.ktor.http.ContentType.Application.Json)
        } catch (e: Exception) {
            assertEquals("com.bortxapps.goprocontrollerandroid.domain.data.GoProException: COMMUNICATION_FAILED", e.toString())
        }
    }

    @Test
    fun `getFile should handle success`() = runBlocking {
        ktorClient = KtorClient(createHttpMockClient(HttpStatusCode.OK, body))
        val response = ktorClient.getFile("/path", io.ktor.http.ContentType.Application.Json)
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(body, response.bodyAsText())
    }
}