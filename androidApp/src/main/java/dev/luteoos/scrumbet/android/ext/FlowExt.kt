package dev.luteoos.scrumbet.android.ext

import kotlinx.coroutines.flow.MutableStateFlow

fun <T : Any> MutableStateFlow<T>.poke() {
    this.tryEmit(this.value)
}
