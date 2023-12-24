package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest

import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class KtorInjectionTest {

    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockWebServer = MockWebServer().apply {
            start()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `HttpClient makes a get operation`() = runTest {
        val client = getKtorHttpClient()
        val json = "{ \"name\": \"John\" }"

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        mockWebServer.enqueue(mockResponse)

        assertEquals(json, client.get(mockWebServer.url("/").toString()).bodyAsText())
    }

    @Test
    fun `HttpClient makes a post operation`() = runTest {
        val client = getKtorHttpClient()
        val json = "{ \"name\": \"John\" }"

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(json)
        mockWebServer.enqueue(mockResponse)

        assertEquals(json, client.post(mockWebServer.url("/").toString()).bodyAsText())
    }
}