package dev.luteoos.scrumbet.domain.repository

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.data.dto.RoomStateFrame
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive
import kotlinx.serialization.json.Json

class RoomRepositoryImpl(private val client: HttpClient) : RoomRepository {

    private var session: WebSocketSession? = null
    private val connectionError: MutableSharedFlow<Exception> = MutableSharedFlow()

    override fun getConnectionErrorFlow(): SharedFlow<Exception> = connectionError

    override suspend fun initSession(roomName: String, username: Username, userId: Id): Result<Unit> {
        return try {
            session = client.webSocketSession {
                url("${RoomRepository.Endpoints.RoomSocket(roomName)}?username=$username")
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
                    .filter { it is Frame.Text }
                    .map {
                        (it as Frame.Text).let {
                            Json.decodeFromString(RoomStateFrame.serializer(), it.readText())
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

}