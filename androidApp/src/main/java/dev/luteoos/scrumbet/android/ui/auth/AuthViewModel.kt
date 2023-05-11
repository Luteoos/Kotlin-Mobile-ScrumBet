package dev.luteoos.scrumbet.android.ui.auth

import androidx.lifecycle.viewModelScope
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.core.KState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class AuthViewModel(private val authController: AuthControllerInterface) : BaseViewModel() {

    val isAuthorized: StateFlow<Boolean> = authController.getStateFlow()
        .map {
            when (it) {
                KState.Empty -> false
                is KState.Error -> false
                KState.Loading -> false
                is KState.Success -> {
                    it.value
                }
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    override fun onCleared() {
        authController.onDeInit()
        super.onCleared()
    }
}
