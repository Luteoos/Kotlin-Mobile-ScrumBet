package dev.luteoos.scrumbet.android.ui.splash

sealed class SplashUiState {
    data class Success(val navigateToRoom: Boolean = false) : SplashUiState()
    object AppVersionObsolete : SplashUiState()
    object Loading : SplashUiState()
}
