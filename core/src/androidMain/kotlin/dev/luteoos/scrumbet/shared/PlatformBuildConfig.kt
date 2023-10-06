package dev.luteoos.scrumbet.shared

import dev.luteoos.scrumbet.BuildConfig

actual object PlatformBuildConfig {
    actual fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    actual fun getBaseWsUrl(): String{
        return BuildConfig.BASE_WS_URL
    }
}
