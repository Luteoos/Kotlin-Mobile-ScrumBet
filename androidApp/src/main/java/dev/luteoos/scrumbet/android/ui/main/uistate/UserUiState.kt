package dev.luteoos.scrumbet.android.ui.main.uistate

import dev.luteoos.scrumbet.data.state.UserData

sealed class UserUiState {
    data class Success(val data: UserData) : UserUiState()
    object Loading : UserUiState()
    data class Error(val message: String = "") : UserUiState()
}
