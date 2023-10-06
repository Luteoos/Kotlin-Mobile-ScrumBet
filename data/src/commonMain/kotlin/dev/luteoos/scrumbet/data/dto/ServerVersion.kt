package dev.luteoos.scrumbet.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ServerVersion(val version: String, val websocketUrl: String = "luteoos-scrumbet-poc.northeurope.cloudapp.azure.com:8080") // todo change to error-generating string after updating server
