package dev.luteoos.scrumbet.android.util

class IntegerLabelFormatter {
    private var lastLabel = ""
    fun format(value: Float): String {
        val label = value.toInt().toString()
        return if (label == lastLabel)
            ""
        else {
            lastLabel = label
            label
        }
    }
}
