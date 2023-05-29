package dev.luteoos.scrumbet.domain.repository

import dev.luteoos.scrumbet.data.dto.ServerVersion
import dev.luteoos.scrumbet.domain.repository.interfaces.ServerRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.get
import io.ktor.http.appendPathSegments
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ServerRepositoryImpl(private val baseUrl: String,
                           private val httpClient: HttpClient): ServerRepository {

    override fun getServerVersion(): Flow<ServerVersion?> {
        return flow {
            try {
                httpClient.get(baseUrl){
                    url {
                        appendPathSegments("/version")
                    }
                }.let { response ->
                    if(response.status.value == 200)
                        this.emit( response.body<ServerVersion>())
                    else
                        throw ResponseException(response, "HTTP GET /version ${response.status.value}")
                }
            }catch (e: Exception){
                println(e)
                this.emit(null)
            }
        }
    }
}