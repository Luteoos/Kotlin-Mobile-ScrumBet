package dev.luteoos.scrumbet.core

import platform.Foundation.NSUUID

actual object UUID {
    actual fun getNewUUID(): String {
        return NSUUID().UUIDString
    }
}
