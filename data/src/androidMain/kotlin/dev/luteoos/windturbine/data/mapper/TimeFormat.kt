package dev.luteoos.scrumbet.data.mapper

actual fun String.Companion.timeFormat(format: String, hours: Int, minutes: Int): String {
    return this.format(format, hours, minutes)
}
