package dev.luteoos.scrumbet.data.state

import dev.luteoos.scrumbet.data.Id

sealed class AuthState {
    object InvalidVersion : AuthState()
    data class UserSignedIn(val userData: UserData) : AuthState()
    data class Connected(val userData: UserData, val roomId: Id) : AuthState()
}
