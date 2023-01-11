package dev.luteoos.scrumbet.data.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RoomConfigIncomingFrame(
    @SerialName("userId")
    val roomOwnerId: String,
    val scaleType: String,
    val anonymousVote: Boolean,
    val alwaysVisibleVote: Boolean
)
