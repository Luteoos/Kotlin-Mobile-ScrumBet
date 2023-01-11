package dev.luteoos.scrumbet.android.ext

import android.content.res.TypedArray
import androidx.core.content.res.use
import java.lang.Exception

inline fun TypedArray.useWithTryCatch(block: (TypedArray) -> Unit) {
    this.use {
        try {
            block(it)
        } catch (e: Exception) {
            println(e)
        }
    }
}
