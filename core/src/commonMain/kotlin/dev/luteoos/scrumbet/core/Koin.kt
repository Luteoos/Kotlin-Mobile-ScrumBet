package dev.luteoos.scrumbet.core

import dev.luteoos.scrumbet.di.coreModule
import dev.luteoos.scrumbet.shared.Log
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) = startKoin {
    appDeclaration()
    modules(coreModule)
    Log.d("Koin initialized")
}

/**
 * called by iOS and other non-JVM targets by
 * `KoinKt.doInitKoin()`
 */
fun initKoin() = initKoin {}
