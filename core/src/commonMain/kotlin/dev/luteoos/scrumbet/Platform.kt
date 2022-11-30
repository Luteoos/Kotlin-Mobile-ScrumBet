package dev.luteoos.scrumbet

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
