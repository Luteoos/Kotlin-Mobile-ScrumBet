package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.AuthState
import dev.luteoos.scrumbet.data.state.UserData
import dev.luteoos.scrumbet.domain.repository.interfaces.ServerRepository
import dev.luteoos.scrumbet.preferences.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.core.component.get

class AuthController(preferences: SharedPreferences? = null, serverRepository: ServerRepository? = null) : KController<AuthState, AppException>(), AuthControllerInterface {
    private val preferences: SharedPreferences
    private val repository: ServerRepository
    private val roomIdFlow: MutableStateFlow<String?> = MutableStateFlow(null)

    /**
     *
     * KState.Loading -> Loading, await
     *
     * AuthState.InvalidVersion -> App version invalid, force update client
     *
     * KState.Empty -> No user or room Info, App version valid
     *
     * AuthState.Connected -> User and Room info valid, App version valid, able to connect
     *
     * AuthState.UserSignedIn -> no room info, user valid, App version valid
     */
    override val state: MutableStateFlow<KState<AuthState, AppException>> = MutableStateFlow(KState.Loading)

    init {
        this.preferences = preferences ?: get()
        this.repository = serverRepository ?: get()
        kcontrollerScope.launch {
            combine(this@AuthController.preferences.getUserDataFlow(), roomIdFlow, repository.getServerVersion()) { user, id, version ->
                // TODO add app version check
                if (user != null) {
                    if (id != null)
                        publish(KState.Success(AuthState.Connected))
                    else
                        publish(KState.Success(AuthState.UserSignedIn))
                } else
                    publish(KState.Empty)
            }.collect()
        }
    }

    override fun setRoomConnectionId(id: String) {
        // TODO room id validation
        kcontrollerScope.launch {
            roomIdFlow.emit(id.trim())
        }
    }

    override fun getRoomConnectionId(): String? {
        return roomIdFlow.value
    }

    override fun getUserData(): UserData? {
        return preferences.getUserData()
    }

    override fun disconnect() {
        kcontrollerScope.launch {
            roomIdFlow.emit(null)
        }
    }
}
