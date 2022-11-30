package dev.luteoos.scrumbet.data.mapper

import platform.Foundation.NSString
import platform.Foundation.stringWithFormat

actual fun String.Companion.timeFormat(
    format: String,
    hours: Int,
    minutes: Int
): String {
    return NSString.stringWithFormat(format, hours, minutes)
}
