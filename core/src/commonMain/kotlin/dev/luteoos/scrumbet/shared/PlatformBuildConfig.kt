package dev.luteoos.scrumbet.shared

expect object PlatformBuildConfig : WebSocketBuildConfig {
    fun getBaseUrl(): String

    fun getAppStoreUrl(): String
}

abstract class WebSocketBuildConfig() {
    private var baseWsUrl = "<empty>"

    fun setBaseWebSocketUrl(url: String) {
        baseWsUrl = url.trim()
    }

    fun getBaseWsUrl() = baseWsUrl
}
