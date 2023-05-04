package dev.luteoos.scrumbet.domain.repository.interfaces

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import kotlinx.coroutines.flow.Flow

interface RoomRepositoryInterface {
    suspend fun initSession(roomName: String, username: Username, userId: Id) : Result<Unit>
    fun getIncomingFlow() : Flow<String>
    suspend fun closeSession()
}