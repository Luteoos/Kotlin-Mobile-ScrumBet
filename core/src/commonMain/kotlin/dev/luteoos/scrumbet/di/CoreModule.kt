package dev.luteoos.scrumbet.di

import dev.luteoos.scrumbet.Greeting
import dev.luteoos.scrumbet.preferences.SharedPreferences
import dev.luteoos.scrumbet.preferences.SharedPreferencesImpl
import org.koin.dsl.module

val coreModule = module {
    single<SharedPreferences> { SharedPreferencesImpl() }

//    single<AuthRepository> { AuthRepositoryImpl() }
//    single<TurbineLiveRepository> { TurbineLiveRepositoryImpl(get()) }

    single { Greeting() }
//    single { TestTurbineRepository(get()) }
}
