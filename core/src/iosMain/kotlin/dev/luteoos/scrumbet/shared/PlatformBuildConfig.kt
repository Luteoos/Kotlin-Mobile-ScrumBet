package dev.luteoos.scrumbet.shared

import platform.Foundation.NSBundle

actual object PlatformBuildConfig : WebSocketBuildConfig() {
    actual fun getBaseUrl(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("API_BASE_URL") ?: "NO-API-BASE-URL").toString()
    }
}
