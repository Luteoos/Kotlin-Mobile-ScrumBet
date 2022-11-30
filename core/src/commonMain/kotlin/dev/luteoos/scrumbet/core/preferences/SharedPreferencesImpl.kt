package dev.luteoos.scrumbet.core.preferences

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import com.russhwolf.settings.set
import dev.luteoos.scrumbet.data.entity.ServerInfoEntity
import dev.luteoos.scrumbet.data.entity.TurbineEntity
import kotlinx.serialization.KSerializer

class SharedPreferencesImpl : SharedPreferences {
    private val settings: Settings = Settings()

    private val serverInfo = "WT_SERVER_INFO"
    private val turbineInfo = "WT_TURBINE_NAME"

    private fun <T>setSerializable(key: String, value: T, serializer: KSerializer<T>) = settings.encodeValue(serializer, key, value)
    private fun <T>getSerializable(key: String, defaultValue: T, serializer: KSerializer<T>) = settings.decodeValue(serializer, key, defaultValue)
    private fun getString(key: String): String? = settings[key]
    private fun setString(key: String, value: String) = settings.set(key, value)
    private fun getBoolean(key: String): Boolean? = settings[key]
    private fun setBoolean(key: String, value: Boolean) = settings.set(key, value)
    private fun clear(key: String) = settings.remove(key)

    override fun getServerInfo(): ServerInfoEntity {
        return getSerializable(serverInfo, ServerInfoEntity.getDefault(), ServerInfoEntity.serializer())
    }

    override fun clearServerInfo() {
        clear(serverInfo)
    }

    override fun setServerInfo(value: ServerInfoEntity) {
        setSerializable(serverInfo, value, ServerInfoEntity.serializer())
    }

    override fun getTurbineInfo(): TurbineEntity {
        return getSerializable(turbineInfo, TurbineEntity.getDefault(), TurbineEntity.serializer())
    }

    override fun clearTurbineInfo() {
        clear(turbineInfo)
    }

    override fun setTurbineInfo(value: TurbineEntity) {
        setSerializable(turbineInfo, value, TurbineEntity.serializer())
    }

    override fun clearAll() {
        settings.clear()
    }
}
