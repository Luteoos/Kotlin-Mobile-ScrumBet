package dev.luteoos.scrumbet.preferences

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.russhwolf.settings.serialization.decodeValue
import com.russhwolf.settings.serialization.encodeValue
import com.russhwolf.settings.set
import dev.luteoos.scrumbet.data.state.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.KSerializer

class SharedPreferencesImpl : SharedPreferences {
    private val settings: Settings = Settings()
    private val userDataFlow = MutableStateFlow<UserData?>(getUserData())

    private fun <T>setSerializable(key: String, value: T, serializer: KSerializer<T>) = settings.encodeValue(serializer, key, value)
    private fun <T>getSerializable(key: String, defaultValue: T, serializer: KSerializer<T>) = settings.decodeValue(serializer, key, defaultValue)
    private fun getString(key: String): String? = settings[key]
    private fun setString(key: String, value: String) = settings.set(key, value)
    private fun getBoolean(key: String): Boolean? = settings[key]
    private fun setBoolean(key: String, value: Boolean) = settings.set(key, value)
    private fun clear(key: String) = settings.remove(key)

    override fun setUserData(userData: UserData) {
        setSerializable(keyUserData, userData, UserData.serializer())
        CoroutineScope(Dispatchers.Main).launch {
            userDataFlow.emit(userData)
        }
    }

    override fun getUserData(): UserData? {
        return getSerializable(keyUserData, UserData.getEmpty(), UserData.serializer()).let { data ->
            if (data.isEmpty())
                null
            else
                data
        }
    }

    override fun getUserDataFlow(): StateFlow<UserData?> {
        return userDataFlow
    }

    override fun clearUserData() {
        clear(keyUserData)
    }

    override fun clearAll() {
        settings.clear()
    }

    companion object {
        private const val keyUserData = "SBUsername"
    }
}
