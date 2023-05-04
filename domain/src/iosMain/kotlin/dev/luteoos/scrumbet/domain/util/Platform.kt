package dev.luteoos.scrumbet.domain.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin

actual fun getPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) : HttpClient = HttpClient(Darwin) {
    config(this)
    configureRequest {
        setAllowsCellularAccess(true)
    }
}