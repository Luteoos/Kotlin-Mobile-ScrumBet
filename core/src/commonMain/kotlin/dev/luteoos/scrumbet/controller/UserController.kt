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
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.component.get

class UserController() : KController<UserData, AppException>(), UserControllerInterface {
    private val preferences: SharedPreferences = get<SharedPreferences>()
    private var id: Id? = null

    override val state: MutableStateFlow<KState<UserData, AppException>> = MutableStateFlow(KState.Loading())

    init {
        getUserData()
    }

    override fun updateUsername(username: Username) {
        publish(KState.Loading())
        id?.let { id ->
            preferences.setUsername(UserData(username, id))
        }
        getUserData()
    }

    private fun getUserData() {
        preferences.getUserData().let { user ->
            if (user == null){
                id = UUID.getNewUUID()
                publish(KState.Empty()) // State.Empty -> UI prompt for new username
            }
            else {
                id = user.userId
                publish(KState.Success(user))
            }
        }
    }
}

fun a() {
    val a: UserControllerInterface = UserController()
    a.watchState()
    a.getStateFlow()
    a.onStart()
}
