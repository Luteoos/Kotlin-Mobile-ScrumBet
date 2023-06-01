package dev.luteoos.scrumbet.domain.repository

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.data.dto.RoomConfigOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomResetOutgoingFrame
import dev.luteoos.scrumbet.data.dto.RoomStateFrame
import dev.luteoos.scrumbet.data.dto.RoomVoteFrame
import dev.luteoos.scrumbet.data.dto.RoomVoteOutgoingFrame
import dev.luteoos.scrumbet.domain.repository.interfaces.RoomRepository
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import io.ktor.websocket.serialization.sendSerializedBase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class RoomRepositoryImpl(private val client: HttpClient,
                         private val baseUrl: String) : RoomRepository {

    private var session: WebSocketSession? = null
    private val connectionError: MutableSharedFlow<Exception> = MutableSharedFlow()
    private val serializer = Json { encodeDefaults = true }
    override fun getConnectionErrorFlow(): SharedFlow<Exception> = connectionError

    override suspend fun initSession(roomName: String, username: Username, userId: Id): Result<Unit> {
        return try {
            session = client.webSocketSession {
                url("${RoomRepository.Endpoints.RoomSocket(baseUrl, roomName).url}?username=$username")
                headers {
                    append("userID", userId)
                }
            }
            if(session?.isActive == true) {
                Result.success(Unit)
            } else Result.failure(Exception("Couldn't establish a connection."))
        } catch(e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
    }

    override suspend fun closeSession() {
        session?.close(reason = CloseReason(CloseReason.Codes.NORMAL, "client disconnecting"))
    }

    override suspend fun observeIncomingFlow() : Flow<RoomStateFrame>{
        return session?.let { wsSession ->
            try {
                wsSession.incoming
                    .receiveAsFlow()
                    .catch {
                        it.printStackTrace()
                        connectionError.emit(Exception(it))
                    }
                    .filter { it is Frame.Text }
                    .map {
                        (it as Frame.Text).let {
                            serializer.decodeFromString(RoomStateFrame.serializer(), it.readText())
                        }
//                        it
                    }
            }catch (e: Exception){
                e.printStackTrace()
                connectionError.emit(e)
                flow { }
            }
        } ?: flow { }
    }

    override suspend fun sendFrame(vote: RoomVoteOutgoingFrame) {
        session?.send(serializer.encodeToString(vote))
    }

    override suspend fun sendFrame(config: RoomConfigOutgoingFrame) {
        session?.send(serializer.encodeToString(config))
    }

    override suspend fun sendFrame(reset: RoomResetOutgoingFrame) {
        session?.send(serializer.encodeToString(reset))
    }
}