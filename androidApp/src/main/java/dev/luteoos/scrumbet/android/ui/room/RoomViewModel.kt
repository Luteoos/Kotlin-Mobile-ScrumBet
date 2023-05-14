package dev.luteoos.scrumbet.android.ui.room

import androidx.lifecycle.viewModelScope
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.controller.interfaces.RoomControllerInterface
import kotlinx.coroutines.launch

class RoomViewModel(
    private val roomController: RoomControllerInterface,
    private val authController: AuthControllerInterface
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            roomController.getStateFlow()
                .collect {
                    it
                }
        }
    }

    fun connect() {
        authController.getRoomConnectionId()?.let {
            roomController.connect(it)
        }
    }

    fun disconnect() {
        roomController.disconnect()
        authController.disconnect()
    }

    override fun onCleared() {
        roomController.onDeInit()
        super.onCleared()
    }
}
