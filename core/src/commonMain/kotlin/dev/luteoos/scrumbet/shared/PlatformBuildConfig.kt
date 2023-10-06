package dev.luteoos.scrumbet.shared

expect object PlatformBuildConfig {
    fun getBaseUrl(): String
    fun getBaseWsUrl(): String
}
