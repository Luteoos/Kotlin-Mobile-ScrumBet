package dev.luteoos.scrumbet.data.state

sealed class AuthState {
    object InvalidVersion : AuthState()
    object UserSignedIn : AuthState()
    object Connected : AuthState()
}
