package dev.luteoos.scrumbet.data.dto

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.UnicodeString
import dev.luteoos.scrumbet.data.Username

@kotlinx.serialization.Serializable
data class RoomVoteFrame(val userId: Id, val username: Username, val vote: UnicodeString?)