package dev.luteoos.scrumbet

class OsXPlatform: Platform{
    override val name: String
        get() = "MacOS"

}

actual fun getPlatform(): Platform  = OsXPlatform()