package dev.luteoos.scrumbet.domain.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.websocket.WebSockets

actual fun getPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) : HttpClient = HttpClient(OkHttp){
    config(this)
}