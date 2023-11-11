package com.bortxapps.goprocontrollerandroid.infrastructure.wifi.rest

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

private const val TIME_OUT = 10_000L
private var ktorHttpClient: HttpClient? = null

fun getKtorHttpClient(): HttpClient {
    return ktorHttpClient ?: privateGenerateClient().also { ktorHttpClient = it }
}

fun privateGenerateClient() = HttpClient(CIO) {
    engine {
        requestTimeout = TIME_OUT
    }

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                isLenient = true
            }
        )
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }

    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
            }
        )
    }

    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                Log.v("Logger Ktor =>", message)
            }
        }
        // TODO change to NONE
        level = LogLevel.ALL
    }
}