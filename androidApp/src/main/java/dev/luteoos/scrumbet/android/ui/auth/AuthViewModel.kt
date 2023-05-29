package dev.luteoos.scrumbet.android.ui.auth

import androidx.lifecycle.viewModelScope
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.state.AuthState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * TODO maybe use authController inside viewModels instead one viewModel
 */
@Deprecated("Deprecated, use AuthController directly", ReplaceWith("AuthControllerInterface"))
class AuthViewModel(private val authController: AuthControllerInterface) : BaseViewModel() {

    val isAuthorized: StateFlow<Boolean> = authController.getStateFlow()
        .map { state ->
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
        }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    override fun onCleared() {
        // onDeInit authController bc AuthViewModel is scoped to activity
//        authController.onDeInit()
        super.onCleared()
    }
}
