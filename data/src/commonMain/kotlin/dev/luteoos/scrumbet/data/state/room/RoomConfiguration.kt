package dev.luteoos.scrumbet.data.state.room

data class RoomConfiguration(
    val isOwner: Boolean,
    val scale: List<String>,
    val scaleType: String,
    val scaleTypeList: List<String>,
    val voteEnded: Boolean,
    val anonymousVote: Boolean,
    val alwaysVisibleVote: Boolean
)
