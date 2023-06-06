package dev.luteoos.scrumbet.domain.repository

import dev.luteoos.scrumbet.data.dto.ServerVersion
import dev.luteoos.scrumbet.domain.repository.interfaces.ServerRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ServerRepositoryImpl(private val baseUrl: String,
                           private val sslPrefix: String,
                           private val httpClient: HttpClient): ServerRepository {

    private val versionFlow: MutableSharedFlow<ServerVersion?> = MutableSharedFlow()

    override fun getServerVersionFlow(): Flow<ServerVersion?> {
        return versionFlow.also {
            fetchServerVersion()
        }
    }

    override fun fetchServerVersion(){
        CoroutineScope(Dispatchers.Unconfined).launch {
            try {
                httpClient.get("$sslPrefix$baseUrl"){
                    url {
                        appendPathSegments("version")
                    }
                }.let { response ->
                    if(response.status.value == 200)
                        versionFlow.emit( response.body<ServerVersion>())
                    else
                        throw ResponseException(response, "HTTP GET /version ${response.status.value}")
                }
            }catch (e: Exception){
                println(e)
                versionFlow.emit(null)
            }
        }
    }
}