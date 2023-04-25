package dev.luteoos.scrumbet.data.state.room

data class RoomUser(
    val userId: String,
    val username: String,
    val isOwner: Boolean,
    val vote: String?
)
