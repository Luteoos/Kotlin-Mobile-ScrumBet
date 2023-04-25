package dev.luteoos.scrumbet.data.state.room

data class RoomData(
    val configuration: RoomConfiguration,
    val voteList: List<RoomUser>
)
