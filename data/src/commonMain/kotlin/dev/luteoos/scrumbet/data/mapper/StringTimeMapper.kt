package dev.luteoos.scrumbet.data.mapper

expect fun String.Companion.timeFormat(format: String = "%02d:%02d", hours: Int, minutes: Int): String
