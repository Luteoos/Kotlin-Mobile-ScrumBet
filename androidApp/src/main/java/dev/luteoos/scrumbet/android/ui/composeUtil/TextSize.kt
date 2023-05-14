package dev.luteoos.scrumbet.android.ui.composeUtil

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

object TextSize {
    fun small() = getTextUnit(17f)
    fun xSmall() = getTextUnit(15f)
    fun large() = getTextUnit(34f)

    private fun getTextUnit(value: Float) = TextUnit(value, TextUnitType.Sp)
}
