package dev.luteoos.scrumbet.domain.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun getHttpClient() = getPlatformHttpClient {
    followRedirects = true

    install(Logging){
        logger = Logger.DEFAULT
        level = LogLevel.ALL
        sanitizeHeader { header -> header == HttpHeaders.Authorization }
    }
    install(WebSockets){
        pingInterval = 5_000
        contentConverter = KotlinxWebsocketSerializationConverter(Json)
    }
    install(ContentNegotiation){
        json(Json {
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }
    engine {
        threadsCount = 1
    }
    request {
        headers{

        }
    }
}

expect fun getPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) : HttpClient