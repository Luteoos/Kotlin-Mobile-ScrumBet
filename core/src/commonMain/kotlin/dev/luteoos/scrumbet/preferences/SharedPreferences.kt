package dev.luteoos.scrumbet.preferences

import dev.luteoos.scrumbet.data.state.UserData
import kotlinx.coroutines.flow.StateFlow

interface SharedPreferences {
    fun setUserData(userData: UserData)
    fun getUserData(): UserData?
    fun getUserDataFlow(): StateFlow<UserData?>
    fun clearUserData()
    fun clearAll()
}
