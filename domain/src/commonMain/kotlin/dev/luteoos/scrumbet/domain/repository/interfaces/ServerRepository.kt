package dev.luteoos.scrumbet.domain.repository.interfaces

import dev.luteoos.scrumbet.data.dto.ServerVersion
import kotlinx.coroutines.flow.Flow

interface ServerRepository {
    fun getServerVersionFlow(): Flow<ServerVersion?>
    fun fetchServerVersion()
}