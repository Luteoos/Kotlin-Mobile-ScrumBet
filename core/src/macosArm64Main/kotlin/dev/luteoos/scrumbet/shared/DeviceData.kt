package dev.luteoos.scrumbet.shared

import platform.Foundation.NSProcessInfo

actual class DeviceData actual constructor() {
    actual fun getDeviceName(): String {
        return NSProcessInfo.processInfo.hostName.replace(".", "-")
    }
}
