package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.data.state.AuthState
import dev.luteoos.scrumbet.data.state.UserData
import dev.luteoos.scrumbet.domain.repository.interfaces.ServerRepository
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.shared.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.core.qualifier.named

class AuthController(
    preferences: SharedPreferences? = null,
    serverRepository: ServerRepository? = null,
    applicationVersion: String? = null
) : KController<AuthState, AppException>(), AuthControllerInterface {
    private val preferences: SharedPreferences
    private val repository: ServerRepository
    private val appVersion: String
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
        this.appVersion = applicationVersion ?: get(named("APP_VERSION"))
        kcontrollerScope.launch(Dispatchers.Default) {
            combine(this@AuthController.preferences.getUserDataFlow(), roomIdFlow, repository.getServerVersionFlow()) { user, id, version ->
                if (true) { // false to skip when not running server
                    if (version == null) {
                        publish(KState.Error(AppException.GeneralException()))
                        return@combine
                    }
                    if (version.version != appVersion) {
                        publish(KState.Success(AuthState.InvalidVersion))
                        return@combine
                    }
                }
                if (user != null) {
                    Log.i("Version server($version) app($appVersion)")
                    if (id != null)
                        publish(KState.Success(AuthState.Connected(user, id)))
                    else
                        publish(KState.Success(AuthState.UserSignedIn(user)))
                } else
                    publish(KState.Empty)
            }.collect()
        }
    }

    override fun setRoomConnectionId(id: String) {
        // TODO room id validation
        kcontrollerScope.launch {
            roomIdFlow.emit(id.trim().split("/").lastOrNull())
        }
    }

    override fun getRoomConnectionId(): String? {
        return roomIdFlow.value
    }

    override fun getUserData(): UserData? {
        return preferences.getUserData()
    }

    override fun retry() {
        repository.fetchServerVersion()
    }

    override fun disconnect() {
        kcontrollerScope.launch {
            roomIdFlow.emit(null)
        }
    }
}
