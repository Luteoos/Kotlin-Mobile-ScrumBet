package dev.luteoos.scrumbet.data.dto

@kotlinx.serialization.Serializable
data class RoomStateFrame(
    val config: RoomConfigIncomingFrame,
    val list: List<RoomVoteFrame>
)
