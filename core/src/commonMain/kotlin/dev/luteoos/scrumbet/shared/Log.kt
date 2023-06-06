package dev.luteoos.scrumbet.shared

expect class PlatformLogger() {
    fun log(tag: Level, message: String, e: Exception? = null)
}

object Log {
    private val logger = PlatformLogger()

    fun e(e: Exception) {
        logger.log(Level.ERROR, e.message ?: "", e)
    }

    fun w(message: String) {
        logger.log(Level.WARN, message)
    }

    fun i(message: String) {
        logger.log(Level.INFO, message)
    }

    fun d(message: String) {
        logger.log(Level.DEBUG, message)
    }
}

enum class Level(val tag: String) {
    ERROR("error"),
    WARN("warning"),
    INFO("info"),
    DEBUG("debug")
}
