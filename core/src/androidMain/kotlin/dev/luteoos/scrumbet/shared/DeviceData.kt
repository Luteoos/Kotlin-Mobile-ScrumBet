package dev.luteoos.scrumbet.shared

actual class DeviceData {
    actual fun getDeviceName(): String {
        return android.os.Build.MODEL
    }
}
