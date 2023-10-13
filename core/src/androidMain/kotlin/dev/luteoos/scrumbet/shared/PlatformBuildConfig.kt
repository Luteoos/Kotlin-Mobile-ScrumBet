package dev.luteoos.scrumbet.shared

import dev.luteoos.scrumbet.BuildConfig

actual object PlatformBuildConfig : WebSocketBuildConfig() {
    actual fun getBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    actual fun getAppStoreUrl(): String {
        return BuildConfig.APP_STORE_URL
    }
}
