package dev.luteoos.scrumbet.domain.repository.interfaces

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.data.dto.RoomConfigOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomResetOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomStateFrame
import dev.luteoos.scrumbet.data.dto.RoomVoteFrame
import io.ktor.websocket.Frame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface RoomRepository {

    fun getConnectionErrorFlow(): SharedFlow<Exception>
    suspend fun initSession(roomName: String, username: Username, userId: Id) : Result<Unit>
    suspend fun closeSession()
    suspend fun observeIncomingFlow(): Flow<RoomStateFrame>
    suspend fun sendFrame(vote: RoomVoteFrame)

    suspend fun sendFrame(config: RoomConfigOutgoingFrame)

    suspend fun sendFrame(reset: RoomResetOutgoingFrame)

    sealed class Endpoints(val url: String) {
        class RoomSocket(baseUrl:String, roomId: String): Endpoints("ws://$baseUrl/room/$roomId")
    }

    fun isSessionActive(): Boolean
}