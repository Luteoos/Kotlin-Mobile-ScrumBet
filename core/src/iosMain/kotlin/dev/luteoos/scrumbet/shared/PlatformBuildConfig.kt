package dev.luteoos.scrumbet.shared

import platform.Foundation.NSBundle

actual object PlatformBuildConfig : WebSocketBuildConfig(), PlatformBuildConfigInterface {
    actual override fun getBaseUrl(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("API_BASE_URL") ?: "NO-API-BASE-URL").toString()
    }

    actual override fun getAppStoreUrl(): String {
        return (NSBundle.mainBundle.infoDictionary?.get("APP_STORE_URL") ?: "NO-APP-STORE-URL").toString()
    }
}
