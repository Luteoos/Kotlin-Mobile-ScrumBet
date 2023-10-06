package dev.luteoos.scrumbet.shared

import platform.Foundation.NSBundle

actual object PlatformBuildConfig : WebSocketBuildConfig() {
    actual fun getBaseUrl(): String {
        // add "API_BASE_URL" to Target -> Info -> Custom MacOS Properties
        return (NSBundle.mainBundle.infoDictionary?.get("API_BASE_URL") ?: "NO-API-BASE-URL").toString()
    }
}
