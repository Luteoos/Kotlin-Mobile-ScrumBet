package dev.luteoos.scrumbet.controller.interfaces

import dev.luteoos.scrumbet.core.KControllerInterface
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.room.RoomData

interface RoomControllerInterface : KControllerInterface<RoomData, AppException> {
    fun connect(roomId: String)
    fun vote(voteValue: String)
    fun displayValues(showValues: Boolean)
    fun resetRoom()
    fun setRoomScale(scale: String)
    fun disconnect()
}
