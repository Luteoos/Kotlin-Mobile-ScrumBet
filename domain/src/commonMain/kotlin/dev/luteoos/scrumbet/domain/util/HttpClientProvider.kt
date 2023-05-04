package dev.luteoos.scrumbet.domain.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.headers
import io.ktor.client.request.request

fun getHttpClient() = getPlatformHttpClient {
    install(WebSockets){
        pingInterval = 5_000
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