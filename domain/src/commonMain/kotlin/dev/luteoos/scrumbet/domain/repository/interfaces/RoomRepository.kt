package dev.luteoos.scrumbet.domain.repository.interfaces

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.data.dto.RoomStateFrame
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface RoomRepository {

    fun getConnectionErrorFlow(): SharedFlow<Exception>
    suspend fun initSession(roomName: String, username: Username, userId: Id) : Result<Unit>
    suspend fun closeSession()
    suspend fun observeIncomingFlow(): Flow<RoomStateFrame>

    companion object {
        const val BASE_URL = "ws://192.168.0.2:8080"
    }

    sealed class Endpoints(val url: String) {
        class RoomSocket(roomId: String): Endpoints("$BASE_URL/room/$roomId")
    }
}