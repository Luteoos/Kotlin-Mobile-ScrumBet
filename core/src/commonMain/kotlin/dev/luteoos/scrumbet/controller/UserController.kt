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
import org.koin.core.component.inject

class UserController : KController<UserData, AppException>(), UserControllerInterface {
    private val preferences by inject<SharedPreferences>()
    private var id: Id = UUID.getNewUUID()

    override val state: MutableStateFlow<KState<UserData, AppException>> = MutableStateFlow(KState.Loading())

    init {
        getUserData()
    }

    override fun updateUsername(username: Username) {
        preferences.setUsername(UserData(username, id))
        getUserData()
    }

    private fun getUserData() {
        preferences.getUserData().let { user ->
            if (user == null)
                publish(KState.Empty())
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
