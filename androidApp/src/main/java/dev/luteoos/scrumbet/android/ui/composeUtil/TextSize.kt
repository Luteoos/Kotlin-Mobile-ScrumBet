package dev.luteoos.scrumbet.android.ui.composeUtil

import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

object TextSize {

    /**
     * 17sp
     */
    fun small() = getTextUnit(17f)

    /**
     * 15sp
     */
    fun xSmall() = getTextUnit(15f)

    /**
     * 12sp
     */
    fun xxSmall() = getTextUnit(12f)

    /**
     * 28sp
     */
    fun regular() = getTextUnit(28f)

    /**
     * 34sp
     */
    fun large() = getTextUnit(34f)

    /**
     * 40sp
     */
    fun xLarge() = getTextUnit(40f)

    private fun getTextUnit(value: Float) = TextUnit(value, TextUnitType.Sp)
}
