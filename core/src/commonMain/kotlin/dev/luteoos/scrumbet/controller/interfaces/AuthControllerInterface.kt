package dev.luteoos.scrumbet.controller.interfaces

import dev.luteoos.scrumbet.core.KControllerInterface
import dev.luteoos.scrumbet.data.entity.AppException

interface AuthControllerInterface : KControllerInterface<Boolean, AppException> {
    fun setRoomConnectionId(id: String)
    fun getRoomConnectionId(): String?
    fun disconnect()
}
