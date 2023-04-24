package dev.luteoos.scrumbet.controller.interfaces

import dev.luteoos.scrumbet.core.CFlow
import dev.luteoos.scrumbet.core.KControllerInterface
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.UserData

interface UserControllerInterface : KControllerInterface<UserData, AppException> {
    fun updateUsername(username: Username)

    /**
     * Returns `KState<UserData, AppException>`
     */
    override fun watchState(): CFlow<KState<UserData, AppException>>
}
