package dev.luteoos.scrumbet.shared

import platform.UIKit.UIDevice

actual class DeviceData {
    actual fun getDeviceName(): String {
        return UIDevice.current.name
    }
}
