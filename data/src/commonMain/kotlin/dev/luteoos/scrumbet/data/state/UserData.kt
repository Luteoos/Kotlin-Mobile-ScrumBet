package dev.luteoos.scrumbet.data.state

import dev.luteoos.scrumbet.data.Id
import dev.luteoos.scrumbet.data.Username

@kotlinx.serialization.Serializable
data class UserData(
    val username: Username,
    val userId: Id
) {

    fun isEmpty(): Boolean = username.isEmpty() && userId.isEmpty()

    companion object {
        fun getEmpty(): UserData {
            return UserData("", "")
        }
    }
}
