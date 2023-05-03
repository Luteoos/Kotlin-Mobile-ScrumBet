package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.UserControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.core.UUID
import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.UserData
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.shared.DeviceData
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.get

class UserController(preferences: SharedPreferences? = null, deviceData: DeviceData? = null) : KController<UserData, AppException>(), UserControllerInterface {
    private val preferences: SharedPreferences
    private val deviceData: DeviceData
    private var id: Id? = null

    override val state: MutableStateFlow<KState<UserData, AppException>> = MutableStateFlow(KState.Loading)

    init {
        this.preferences = preferences ?: get()
        this.deviceData = deviceData ?: get()
        getUserData()
    }

    override fun updateUsername(username: Username) {
        publish(KState.Loading)
        id?.let { id ->
            preferences.setUserData(UserData(username, id))
        }
        getUserData()
    }

    fun test(): String {
//        return preferences.toString()
        return id ?: "none"
    }

    fun getUserData() {
        preferences.getUserData().let { user ->
            if (user == null) {
                generateDefaultUserData()
                getUserData()
            } else {
                id = user.userId
                publish(KState.Success(user))
            }
        }
    }

    private fun generateDefaultUserData() {
        publish(KState.Loading)
        preferences.setUserData(
            UserData(
                username = deviceData.getDeviceName(),
                userId = UUID.getNewUUID()
            )
        )
    }
}
