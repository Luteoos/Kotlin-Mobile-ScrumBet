package dev.luteoos.scrumbet.shared

import platform.Foundation.NSBundle

actual object PlatformBuildConfig {
    actual fun getBaseUrl(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("API_BASE_URL") ?: "NO-API-BASE-URL").toString()
    }

    actual fun getBaseWsUrl(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("API_BASE_WS_URL") ?: "NO-API-BASE-URL").toString()
    }
}
