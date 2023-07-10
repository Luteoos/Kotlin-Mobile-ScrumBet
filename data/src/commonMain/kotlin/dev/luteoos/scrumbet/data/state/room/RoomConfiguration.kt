package dev.luteoos.scrumbet.data.state.room

import dev.luteoos.scrumbet.data.entity.MultiUrl

data class RoomConfiguration(
    val url: MultiUrl,
    val roomJoinCode: String,
    val isOwner: Boolean,
    val scale: List<String>,
    val scaleType: String,
    val scaleTypeList: List<String>,
    val voteEnded: Boolean,
    val anonymousVote: Boolean,
    val alwaysVisibleVote: Boolean
)
