package dev.luteoos.scrumbet.shared

interface PlatformBuildConfigInterface {
    fun getBaseUrl(): String

    fun getAppStoreUrl(): String

    fun setBaseWebSocketUrl(url: String)
}

expect object PlatformBuildConfig : WebSocketBuildConfig, PlatformBuildConfigInterface {
    override fun getBaseUrl(): String

    override fun getAppStoreUrl(): String
}

abstract class WebSocketBuildConfig() {
    private var baseWsUrl = "<empty>"

    fun setBaseWebSocketUrl(url: String) {
        baseWsUrl = url.trim()
    }

    fun getBaseWsUrl() = baseWsUrl
}
