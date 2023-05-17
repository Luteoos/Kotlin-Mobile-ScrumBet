package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.RoomControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KHandler
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.room.RoomConfiguration
import dev.luteoos.scrumbet.data.state.room.RoomData
import dev.luteoos.scrumbet.data.state.room.RoomUser
import kotlinx.coroutines.flow.MutableStateFlow

class RoomController : KController<RoomData, AppException>(), RoomControllerInterface {
    override val state: MutableStateFlow<KState<RoomData, AppException>> = MutableStateFlow(KState.Empty)

    override fun connect(roomId: String) {
        publish(KState.Loading)
        KHandler().postDelayed({
            if (roomId == "true")
                publish(KState.Success(getMockRoomData()))
            else
                publish(KState.Error(AppException.GeneralException("404")))
        }, 5000)
    }

    override fun vote(voteValue: String) {
        TODO("Not yet implemented")
    }

    override fun displayValues(endVote: Boolean) {
        TODO("Not yet implemented")
    }

    override fun resetRoom() {
        TODO("Not yet implemented")
    }

    override fun setRoomScale() {
        TODO("Not yet implemented")
    }

    override fun disconnect() {
        println("Room controller disconnect")
    }

    private fun getMockRoomData() =
        RoomData(
            RoomConfiguration(
                true,
                listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "11", "?"),
                voteEnded = false,
                anonymousVote = true,
                alwaysVisibleVote = false
            ),
            listOf(
                RoomUser(
                    "id1",
                    "user-user1",
                    false,
                    "2"
                ),
                RoomUser(
                    "id2",
                    "owner-user2",
                    true,
                    "4"
                )
            )
        )
}
