package dev.luteoos.scrumbet.domain

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform