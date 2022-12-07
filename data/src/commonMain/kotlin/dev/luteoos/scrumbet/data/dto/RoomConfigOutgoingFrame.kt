package dev.luteoos.scrumbet.data.dto

import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
data class RoomConfigOutgoingFrame(
    @SerialName("userId")
    val senderId: String,
    val scaleType: String,
    val anonymousVote: Boolean,
    val alwaysVisibleVote: Boolean)
