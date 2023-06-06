package dev.luteoos.scrumbet.shared

import platform.Foundation.NSLog

actual class PlatformLogger {

    //TODO rethink
    actual fun log(tag: Level, message: String, e: Exception?) {
        NSLog("${tag.tag}/ $message")
        if(e != null)
            NSLog("${tag.tag}/ $e")
    }
}
