package dev.luteoos.scrumbet.core

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Coroutine powered delay similar to JVM Handler
 */
class KHandler {

    private var dispatcher: CoroutineDispatcher = Dispatchers.Main
    private var job: Job? = null

    /**
     * change dispatcher from default [Dispatchers.Main]
     */
    fun setDispatcher(dispatchOn: CoroutineDispatcher) {
        dispatcher = dispatchOn
    }

    /**
     * get dispatched [Job]
     */
    fun getJob() = job

    fun postDelayed(function: () -> Unit, time: Long) {
        CoroutineScope(dispatcher).apply {
            job = launch {
                delay(time)
                function()
            }
        }
    }
}
