package dev.luteoos.scrumbet.android.ui.room

import dev.luteoos.scrumbet.data.state.room.RoomConfiguration
import dev.luteoos.scrumbet.data.state.room.RoomUser

sealed class RoomUiState {
    data class Success(
        val config: RoomConfiguration,
        val userList: List<RoomUser>,
        val connectionName: String,
        val userVote: String?
    ) : RoomUiState()
    data class Error(val message: String = "") : RoomUiState()
    object Loading : RoomUiState()
    object Disconnect : RoomUiState()
}
