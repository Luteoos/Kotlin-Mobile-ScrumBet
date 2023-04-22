package dev.luteoos.scrumbet.controller.interfaces

import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.UserData

abstract class UserControllerInterface : KController<UserData, AppException>() {
    abstract fun updateUsername(username: Username)
}
