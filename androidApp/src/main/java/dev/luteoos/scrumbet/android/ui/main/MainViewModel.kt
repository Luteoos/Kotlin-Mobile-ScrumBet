package dev.luteoos.scrumbet.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.android.ext.post
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.controller.interfaces.UserControllerInterface
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.state.AuthState
import kotlinx.coroutines.launch

class MainViewModel(
    private val controller: UserControllerInterface,
    private val authController: AuthControllerInterface
) : BaseViewModel() {

    private val _isAuthorized: MutableLiveData<Boolean> = MutableLiveData(false)
    private val _uiState = MutableLiveData<UserUiState>(UserUiState.Loading)
    val uiState: LiveData<UserUiState> = _uiState
    val isAuthorized: LiveData<Boolean> = _isAuthorized

    init {
        viewModelScope.launch {
            controller.getStateFlow()
                .collect { state ->
                    when (state) {
                        KState.Empty, KState.Loading -> _uiState.post(UserUiState.Loading)
                        is KState.Error -> _uiState.post(UserUiState.Error())
                        is KState.Success -> _uiState.post(UserUiState.Success(state.value))
                    }
                }
        }
        viewModelScope.launch {
            authController.getStateFlow()
                .collect { state ->
                    _isAuthorized.post(
                        when (state) {
                            KState.Empty, KState.Loading, is KState.Error -> false
                            is KState.Success -> {
                                when (state.value) {
                                    AuthState.Connected -> true
                                    AuthState.InvalidVersion -> false
                                    AuthState.UserSignedIn -> false
                                }
                            }
                        }
                    )
                }
        }
    }

    fun updateUsername(username: String) {
        controller.updateUsername(username)
    }

    fun setRoomId(id: String) {
        authController.setRoomConnectionId(id)
    }

    fun createNewRoom() {
        setRoomId("true") // UUID.getNewUUID())
    }
}
