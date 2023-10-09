package dev.luteoos.scrumbet.shared

import platform.Foundation.NSBundle

actual object PlatformBuildConfig : WebSocketBuildConfig() {
    actual fun getBaseUrl(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("API_BASE_URL") ?: "NO-API-BASE-URL").toString()
    }

    actual fun getAppStoreUrl(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("APP_STORE_URL") ?: "NO-APP-STORE-URL").toString()
    }
}
