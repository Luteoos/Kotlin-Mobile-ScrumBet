package dev.luteoos.scrumbet.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.android.ext.post
import dev.luteoos.scrumbet.controller.interfaces.UserControllerInterface
import dev.luteoos.scrumbet.core.KState
import kotlinx.coroutines.launch

class MainViewModel(private val controller: UserControllerInterface) : BaseViewModel() {

    private val _uiState = MutableLiveData<UserUiState>(UserUiState.Loading)
    val uiState: LiveData<UserUiState> = _uiState

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
    }

    fun updateUsername(username: String) {
        controller.updateUsername(username)
    }
}
