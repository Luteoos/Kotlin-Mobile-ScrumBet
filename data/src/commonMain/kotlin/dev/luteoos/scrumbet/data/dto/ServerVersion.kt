package dev.luteoos.scrumbet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServerVersion(val version: String, val websocketUrl: String = "<empty>")
