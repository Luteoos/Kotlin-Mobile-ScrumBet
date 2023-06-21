package dev.luteoos.scrumbet.shared

import platform.SystemConfiguration.SCDynamicStoreCopyComputerName

actual class DeviceData actual constructor() {
    actual fun getDeviceName(): String {
        return SCDynamicStoreCopyComputerName(null, null).toString()
    }
}