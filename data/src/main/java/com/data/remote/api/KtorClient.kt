package com.data.remote.api

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.observer.ResponseObserver
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json

val client = HttpClient(Android) {
    install(ContentNegotiation) {
        json()

        engine {
            connectTimeout = TIME_OUT
            socketTimeout = TIME_OUT
        }
    }


    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println("Network Log : >> $message")
            }

        }
        level = LogLevel.ALL
    }

    install(ResponseObserver) {
        onResponse { response ->
            println("TAG_HTTP_STATUS_LOGGER : >> ${response.status.value}")
        }
    }

    install(DefaultRequest) {
        header(HttpHeaders.ContentType, ContentType.Application.Json)
    }
}

private const val TIME_OUT = 10_000
