package dev.luteoos.scrumbet.shared

import timber.log.Timber

actual class PlatformLogger {
    actual fun log(tag: Level, message: String, e: Exception?) {
        when (tag) {
            Level.ERROR -> Timber.e(e)
            Level.WARN -> Timber.w(message)
            Level.INFO -> Timber.i(message)
            Level.DEBUG -> Timber.d(message)
        }
    }
}
