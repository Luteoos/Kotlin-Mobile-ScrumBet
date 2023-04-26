package dev.luteoos.scrumbet.controller

import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.core.KController
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.entity.AppException
import dev.luteoos.scrumbet.preferences.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.koin.core.component.get

/**
 * Empty - User not authenticated
 *
 * Success(true) - User authenticated, connId set
 *
 * Success(false) - User authenticated, connId not set
 */
class AuthController : KController<Boolean, AppException>(), AuthControllerInterface {
    private val preferences: SharedPreferences = get()
    private val roomIdFlow: MutableStateFlow<String?> = MutableStateFlow(null)

    override val state: MutableStateFlow<KState<Boolean, AppException>> = MutableStateFlow(KState.Empty)

    init {
        kcontrollerScope.launch {
            combine(preferences.getUserDataFlow(), roomIdFlow) { user, id ->
                if (user != null) {
                    if (id != null)
                        publish(KState.Success(true))
                    else
                        publish(KState.Success(false))
                } else
                    publish(KState.Empty)
            }
        }
    }

    override fun setRoomConnectionId(id: String) {
        kcontrollerScope.launch {
            roomIdFlow.emit(id.trim())
        }
    }

    override fun getRoomConnectionId(): String? {
        return roomIdFlow.value
    }

    override fun disconnect() {
        kcontrollerScope.launch {
            roomIdFlow.emit(null)
        }
    }
}
