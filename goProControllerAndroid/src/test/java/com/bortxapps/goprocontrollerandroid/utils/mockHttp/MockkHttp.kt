package com.bortxapps.goprocontrollerandroid.utils.mockHttp

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.json.Json

fun createHttpMockClient(status: HttpStatusCode, body: String = "{\"name\":\"John\"}"): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler {
                respond(
                    content = ByteReadChannel(body),
                    status = status,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                )
            }
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    //DO NOTHING
                }
            }
            level = LogLevel.NONE
        }

        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                }
            )
        }
    }
}

fun createMockHttpClientException(exception: Throwable): HttpClient {
    return HttpClient(MockEngine {
        throw exception
    }) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                }
            )
        }
    }
}