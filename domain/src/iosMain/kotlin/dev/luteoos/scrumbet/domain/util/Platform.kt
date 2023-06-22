package dev.luteoos.scrumbet.domain.util

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.engine.cio.*

actual fun getPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) : HttpClient = HttpClient(CIO) {
    config(this)
//    engine {
//        configureRequest {
//            setAllowsCellularAccess(true)
//
//        }
//    }
}