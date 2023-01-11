package dev.luteoos.scrumbet.preferences

import dev.luteoos.scrumbet.data.state.UserData

interface SharedPreferences {
    fun setUsername(userData: UserData)
    fun getUserData(): UserData?
    fun clearUsername()
    fun clearAll()
}
