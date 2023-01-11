package dev.luteoos.scrumbet.android.ext

import android.graphics.Bitmap
import android.graphics.Canvas

fun Bitmap.mergeWithCenter(inCenter: Bitmap): Bitmap {
    Canvas(this).let {
        val resized = Bitmap.createScaledBitmap(inCenter, it.width / 4, it.height / 4, true)
        it.drawBitmap(resized, (it.width - resized.width) / 2f, (it.height - resized.height) / 2f, null)
    }
    return this
}
