package dev.luteoos.scrumbet.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.launchDefault(block: suspend CoroutineScope.() -> Unit) {
    this.launch(Dispatchers.Default) {
        block()
    }
}
