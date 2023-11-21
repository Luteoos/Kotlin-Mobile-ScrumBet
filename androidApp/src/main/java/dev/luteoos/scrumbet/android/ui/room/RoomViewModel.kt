package dev.luteoos.scrumbet.android.ui.room

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.android.ext.post
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.controller.interfaces.RoomControllerInterface
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.state.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RoomViewModel(
    private val roomController: RoomControllerInterface,
    private val authController: AuthControllerInterface
) : BaseViewModel() {

    private val _uiState = MutableLiveData<RoomUiState>(RoomUiState.Loading)
    val uiState: LiveData<RoomUiState> = _uiState

    init {
        viewModelScope.launch(Dispatchers.Main) {
            combine(roomController.getStateFlow(), authController.getStateFlow()) { state, authState ->
                if (authState is KState.Success && authState.value is AuthState.Connected) {
                    val userId = (authState.value as AuthState.Connected).userData.userId
                    when (state) {
                        KState.Empty, KState.Loading -> RoomUiState.Loading
                        is KState.Error -> RoomUiState.Error(state.error.message ?: "")
                        is KState.Success -> {
                            RoomUiState.Success(
                                state.value.configuration,
                                state.value.voteList,
                                authController.getRoomConnectionId() ?: "",
                                state.value.voteList.firstOrNull { it.userId == userId }?.vote
                            )
                        }
                    }
                } else {
                    RoomUiState.Disconnect
                }
            }
                .collect { uiState ->
                    _uiState.post(uiState)
                }
        }
    }

    fun setVote(value: String) {
        roomController.vote(value)
    }

    fun resetVote() {
        roomController.resetRoom()
    }

    fun showVoteValues() {
        roomController.displayValues(true)
    }

    fun hideVoteValues() {
        roomController.displayValues(false)
    }

    fun setAutoReveal(value: Boolean) {
        roomController.setAutoRevealVotes(value)
    }

    fun setScale(scale: String) {
        roomController.setRoomScale(scale)
    }

    fun connect() {
        authController.getRoomConnectionId()?.let {
            roomController.connect(it)
        } ?: _uiState.post(RoomUiState.Disconnect)
    }

    fun disconnect() {
        roomController.disconnect()
        authController.disconnect()
    }

    fun isAlive() {
        if (!roomController.isSessionActive())
            disconnect()
    }

    override fun onCleared() {
        roomController.onDeInit()
        super.onCleared()
    }
}
