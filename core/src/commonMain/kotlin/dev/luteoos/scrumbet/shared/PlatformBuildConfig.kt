package dev.luteoos.scrumbet.shared

import dev.luteoos.scrumbet.BuildKonfig

interface PlatformBuildConfigInterface {
    fun getBaseUrl(): String

    fun getAppStoreUrl(): String

    fun setBaseWebSocketUrl(url: String)
}

expect object PlatformBuildConfig : AbstractBuildConfig, PlatformBuildConfigInterface {
    override fun getBaseUrl(): String

    override fun getAppStoreUrl(): String
}

abstract class AbstractBuildConfig {
    private var baseWsUrl = "<empty>"

    fun setBaseWebSocketUrl(url: String) {
        baseWsUrl = url.trim()
    }

    fun getBaseWsUrl() = baseWsUrl

    fun getOneSignalAppId() = BuildKonfig.osAppId // todo might not be best solution
}
