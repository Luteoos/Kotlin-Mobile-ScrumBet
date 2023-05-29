package dev.luteoos.scrumbet.android.ui.splash

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.luteoos.scrumbet.android.core.BaseViewModel
import dev.luteoos.scrumbet.controller.interfaces.AuthControllerInterface
import dev.luteoos.scrumbet.core.KState
import dev.luteoos.scrumbet.data.state.AuthState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashViewModel(private val authController: AuthControllerInterface) : BaseViewModel() {

    val onReady: MutableLiveData<SplashUiState> = MutableLiveData(SplashUiState.Loading)

    init {
        viewModelScope.launch {
            authController.getStateFlow()
                .collect { state ->
                    when (state) {
                        KState.Empty -> onReady.postValue(SplashUiState.Success())
                        is KState.Error -> onReady.postValue(SplashUiState.AppVersionObsolete) // TODO other error support
                        KState.Loading -> onReady.postValue(SplashUiState.Loading)
                        is KState.Success -> {
                            when (state.value) {
                                AuthState.Connected -> onReady.postValue(SplashUiState.Success(true))
                                AuthState.InvalidVersion -> onReady.postValue(SplashUiState.AppVersionObsolete)
                                AuthState.UserSignedIn -> onReady.postValue(SplashUiState.Success())
                            }
                        }
                    }
                }
        }
    }
}
