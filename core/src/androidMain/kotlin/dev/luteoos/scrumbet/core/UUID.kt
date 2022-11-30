package dev.luteoos.scrumbet.core

import java.util.UUID

actual object UUID {
    actual fun getNewUUID(): String {
        return UUID.randomUUID().toString()
    }
}
