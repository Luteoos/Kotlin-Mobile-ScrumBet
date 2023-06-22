package dev.luteoos.scrumbet.android.ext

fun Boolean.toggle(): Boolean {
    return this.not()
}
