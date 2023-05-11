package dev.luteoos.scrumbet.controller.interfaces

import dev.luteoos.scrumbet.core.KControllerInterface
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.UserData

interface AuthControllerInterface : KControllerInterface<Boolean, AppException> {
    fun setRoomConnectionId(id: String)
    fun getRoomConnectionId(): String?
    fun getUserData(): UserData?
    fun disconnect()
}
