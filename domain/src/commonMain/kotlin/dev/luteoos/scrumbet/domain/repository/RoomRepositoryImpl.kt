package dev.luteoos.scrumbet.domain.repository

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username
import dev.luteoos.scrumbet.domain.repository.interfaces.RoomRepositoryInterface
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.headers
import io.ktor.client.request.url
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.isActive

class RoomRepositoryImpl(private val client: HttpClient) : RoomRepositoryInterface {

    private var session: WebSocketSession? = null

    override suspend fun initSession(roomName: String, username: Username, userId: Id): Result<Unit> {
        return try {
            session = client.webSocketSession {
                url("${RoomRepositoryInterface.Endpoints.RoomSocket(roomName)}?username=$username")
                headers {
                    append("userId", userId)
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

    override suspend fun observeIncomingFlow() : Flow<Frame>{
        return session?.let { wsSession ->
            try {
                wsSession.incoming
                    .receiveAsFlow()
                    .filter { it is Frame.Text }
                    .map {
                        it
                    }
            }catch (e: Exception){
                // here info about socket dc'd as flow
                e.printStackTrace()
                flow { }
            }
        } ?: flow { }
    }

}