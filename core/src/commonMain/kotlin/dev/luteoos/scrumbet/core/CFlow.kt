package dev.luteoos.scrumbet.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

fun interface Closeable {
    fun close()
}

open class CFlow<T : Any> internal constructor(private val origin: Flow<T>) : Flow<T> by origin {
    fun watch(block: (T) -> Unit): Closeable {
        val job = Job()

        onEach {
            block(it)
        }.launchIn(CoroutineScope(Dispatchers.Main + job))

        return Closeable { job.cancel() }
    }

    companion object {
        fun <T : Any>getMock(mockValue: T): CFlow<T> {
            return CFlow(flow { emit(mockValue) })
        }
    }
}

internal fun <T : Any> Flow<T>.wrap(): CFlow<T> = CFlow(this)
