package dev.luteoos.scrumbet.domain.repository.interfaces

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.Flow

interface RoomRepositoryInterface {
    suspend fun initSession(roomName: String, username: Username, userId: Id) : Result<Unit>
    suspend fun closeSession()
    suspend fun observeIncomingFlow(): Flow<Frame>

    companion object {
        const val BASE_URL = "ws://192.168.0.2:8082"
    }

    sealed class Endpoints(val url: String) {
        class RoomSocket(roomId: String): Endpoints("$BASE_URL/$roomId")
    }
}