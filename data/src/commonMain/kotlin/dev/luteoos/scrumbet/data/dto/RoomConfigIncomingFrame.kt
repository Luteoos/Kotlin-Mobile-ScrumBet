package dev.luteoos.scrumbet.data.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RoomConfigIncomingFrame(
    @SerialName("userId")
    val roomOwnerId: String,
    val scaleList: List<String>,
    val scaleType: String,
    @SerialName("scaleTypes")
    val scaleTypeList: List<String>,
    val roomCode: String,
    val anonymousVote: Boolean,
    val alwaysVisibleVote: Boolean,
    val voteEnded: Boolean = false,
    val autoRevealVotes: Boolean = true
)
